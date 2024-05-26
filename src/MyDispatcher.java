/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {
    int lastHost = 0;
    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }

    @Override
    public void addTask(Task task) {
      //  System.out.println(algorithm.equals(SchedulingAlgorithm.ROUND_ROBIN));

           // System.out.println(Timer.getTimeDouble() + " " + task.getId() + " " + task.getStart() + task.isPreemptible());
            //task.setLeft(task.getDuration());
           // System.out.println(task.getLeft());
           /* while(task.getLeft() > 0){
                double c = Timer.getTimeDouble();


                System.out.println(task.getLeft() + " " + task.getId());
                task.setLeft((long) (task.getLeft() -  Timer.getTimeDouble() + c));

            } */

        if (algorithm.equals(SchedulingAlgorithm.ROUND_ROBIN)) {
            synchronized (Host.class) {
                hosts.get(lastHost % hosts.size()).addTask(task);

                lastHost++;
            }
        }

        if (algorithm.equals(SchedulingAlgorithm.SHORTEST_QUEUE)) {
            synchronized (Host.class) {
                Host j = hosts.get(0);
                for(Host i : hosts) {
                    if(i.getQueueSize() < j.getQueueSize())
                        j = i;

                    if(((MyHost)i).getQueueSize() == j.getQueueSize() && ((MyHost)j).isRunning() == true
                                    && ((MyHost)i).isRunning() == false){
                        j=i;
                    }
                }

                j.addTask(task);
            }



        }

        if (algorithm.equals(SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT)){

            if(task.getType().equals(TaskType.SHORT)) {
                hosts.get(0).addTask(task);
            }

            if(task.getType().equals(TaskType.MEDIUM)) {
                hosts.get(1).addTask(task);
            }
            if(task.getType().equals(TaskType.LONG)) {
                hosts.get(2).addTask(task);
            }

        }


        if (algorithm.equals(SchedulingAlgorithm.LEAST_WORK_LEFT)) {
            synchronized (Host.class) {
                Host j = hosts.get(0);
                for(Host i : hosts) {
                 //   System.out.println(i.getWorkLeft() + " " + i.getId() + " fata de " + j.getId() + " " + j.getWorkLeft());
                    if(i.getWorkLeft() < j.getWorkLeft())
                        j = i;

                 //   if(((MyHost)i).getWorkLeft() == j.getWorkLeft() && ((MyHost)j).isRunning() == true
                 //           && ((MyHost)i).isRunning() == false){
                 //       j=i;
                 //   }
                }
             //  System.out.println(task.getId() + " se duce la " + j.getId());
                j.addTask(task);
            }



        }

   }
}
