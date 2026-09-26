package core;

import java.util.List;
import java.util.Map;
import model.Event;

public class Simulator {
    private double globalTime;
    private final Scheduler scheduler;
    private final List<Queue> queues;

    public Simulator(List<Queue> queues, Scheduler scheduler) {
        this.globalTime = 0;
        this.queues = queues;
        this.scheduler = scheduler;
    }

    public double getGlobalTime() {
        return globalTime;
    }

    private void advanceTime(Event event) {
        // Todos os estados permanecem ativos ate o instante do evento.
        for (Queue queue : queues) {
            queue.countTime(event, globalTime);
        }
        globalTime = event.getTime();
    }

    private void scheduleService(Queue queue, int queueIndex) {
        // O tipo do atendimento depende de a fila ser intermediaria ou final.
        if (queueIndex == queues.size() - 1) {
            scheduler.addDeparture(queue, globalTime, queueIndex);
        } else {
            scheduler.addPassage(queue, globalTime, queueIndex);
        }
    }

    private void scheduleNext(Queue current, int currentIndex) {
        if (currentIndex < 0 || currentIndex >= queues.size()) {
            return;
        }

        // O roteamento acontece somente depois que o atendimento termina.
        int nextIndex = current.nextQueue(scheduler.nextRandom());
        if (nextIndex == -1) {
            return;
        }

        Queue next = queues.get(nextIndex);
        if (next.getStatus() >= next.getCapacity()) {
            next.addLoss();
            return;
        }

        next.in();
        if (next.getStatus() <= next.getServers()) {
            scheduleService(next, nextIndex);
        }
    }

    public void ARRIVAL(Event event) {
        int index = event.getQueueIndex();
        Queue queue = queues.get(index);

        // A chegada externa entra na fila, mas nao e roteada.
        advanceTime(event);
        if (queue.getStatus() < queue.getCapacity()) {
            queue.in();
            if (queue.getStatus() <= queue.getServers()) {
                scheduleService(queue, index);
            }
        } else {
            queue.addLoss();
        }

        // Somente a fila 0 possui chegadas externas; Scheduler ignora null.
        scheduler.addArrival(queues.get(0), globalTime, 0);
    }

    public void PASSAGE(Event event) {
        int index = event.getQueueIndex();
        Queue queue = queues.get(index);

        // O atendimento terminou: primeiro atualiza o tempo e remove o cliente.
        advanceTime(event);
        queue.out();

        // Um cliente que espera pode iniciar o proximo atendimento nesta fila.
        if (queue.getStatus() >= queue.getServers()) {
            scheduleService(queue, index);
        }

        // Somente agora o cliente atendido e roteado para a proxima fila.
        scheduleNext(queue, index);
    }

    public void DEPARTURE(Event event) {
        int index = event.getQueueIndex();
        Queue queue = queues.get(index);

        // O atendimento terminou: primeiro atualiza o tempo e remove o cliente.
        advanceTime(event);
        queue.out();

        // Agenda o atendimento do proximo cliente, se houver um aguardando.
        if (queue.getStatus() >= queue.getServers()) {
            scheduler.addDeparture(queue, globalTime, index);
        }

        // O cliente atendido pode seguir para outra fila ou sair do sistema.
        scheduleNext(queue, index);
    }

    public void simulate(int count, double timeFirstEvent) {
        scheduler.addFirstEvent(timeFirstEvent);
        for (Queue queue : queues) {
            queue.populateStatusTimes();
        }

        while (scheduler.getRandomCount() < count) {
            Event event = scheduler.nextEvent();

            switch (event.getType()) {
                case ARRIVAL -> ARRIVAL(event);
                case PASSAGE -> PASSAGE(event);
                case DEPARTURE -> DEPARTURE(event);
            }
        }

        System.out.println("\n--- Simulation Results ---");
        System.out.printf("Total simulated time: %.2f%n", globalTime);

        for (int index = 0; index < queues.size(); index++) {
            Queue queue = queues.get(index);
            System.out.printf("%nQueue %d | Losses: %d%n", index + 1, queue.getLoss());
            System.out.println("Customers |       Time | Time (%)");

            // entrySet tambem cobre filas infinitas, cujos estados sao descobertos em runtime.
            queue.getStatusTimes().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        double time = entry.getValue();
                        double percentage = (time / globalTime) * 100;
                        System.out.printf("%9d | %10.2f | %6.2f%%%n", entry.getKey(), time, percentage);
                    });
        }
    }
}