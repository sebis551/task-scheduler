/* Implement this class. */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class MyHost extends Host {

    private ArrayList<Task> queue = new ArrayList<>();
    private Task currentTask = null;

    private double currentTaskTimer = Timer.getTimeDouble();
    private boolean shouldShutdown = false;
    private SchedulingAlgorithm algorithm;

    public boolean isShouldShutdown() {
        return shouldShutdown;
    }

    public void setShouldShutdown(boolean shouldShutdown) {
        this.shouldShutdown = shouldShutdown;
    }
    public boolean getShouldShutdown() {
       return this.shouldShutdown;
    }

    public void setAlgorithm(SchedulingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void run() {

        while(!isInterrupted()) {

            synchronized (this) {
                if (queue.size() != 0) {
                    if (currentTask == null) {


                        if (queue.size() > 1)
                            queue.sort(new Comparator<Task>() {
                                @Override
                                public int compare(Task obj1, Task obj2) {
                                     if (obj1.getPriority() < obj2.getPriority()) return -1;
                                     if (obj1.getPriority() == obj2.getPriority()) {

                                       return obj2.getStart() - obj1.getStart();

                                    }
                                    return 1;
                                }
                            });






                      //  if(this.getId() == 18)
                       // System.out.println(queue);






                        currentTask = queue.remove(queue.size() - 1);
                     //   System.out.println("a fost ales " + currentTask.getId() + " la " + this.getId());

                        currentTaskTimer = Timer.getTimeDouble();
                    }
                }
            }
           // double c = Timer.getTimeDouble();

            if(currentTask!= null) {

                synchronized (this) {
                    if ((long) ((Timer.getTimeDouble() - currentTaskTimer) * 1000) >= currentTask.getLeft()) {
                        // System.out.println("DA");
                      //  System.out.println(currentTask.getId() + "a terminat");
                        currentTask.finish();
                        currentTask = null;
                    }
                }

            }
        }
       // System.out.println(shouldShutdown);
    }

    public boolean isRunning() {
        if(currentTask != null) return true;
        return false;
    }

    @Override
    public void addTask(Task task) {
     //  System.out.println("" + queue.size() );
      // System.out.println("ajunge tasku" + task.getId() + " " + this.getId());
        if(getQueueSize() == 0 && currentTask == null){
           // System.out.println("1" + " " + task.getId());
           synchronized (this) {
               queue.add(0, task);
            //   System.out.println(task.getId() + "primu 1");
           }
        } else {
            if(currentTask != null && currentTask.isPreemptible() == true) {
              //  System.out.println("2" + " " + task.getId());
                if (task.getPriority() > currentTask.getPriority()) {
                   // System.out.println(currentTask.getId() + " " + task.getId());
                synchronized (this){
                    currentTask.setLeft(currentTask.getLeft() - (long) ((Timer.getTimeDouble() - currentTaskTimer) * 1000));
                    // System.out.println(currentTask.getId() + " " + currentTask.getLeft());
                    queue.add(0, currentTask);
                    currentTask = task;
                    currentTaskTimer = Timer.getTimeDouble();
                   /* queue.sort(new Comparator<Task>() {
                        @Override
                        public int compare(Task obj1, Task obj2) {
                            if (obj1.getPriority() < obj2.getPriority()) return -1;
                           // if (obj1.getPriority() == obj2.getPriority()) {
                           //     if (obj1.getStart() < obj2.getStart()) return 1;

                           // }
                            return 1;
                        }
                    }); */
                }
                } else {
                    synchronized (this){

                        queue.add(0, task);
                      /*  if (queue.size() > 1)
                            queue.sort(new Comparator<Task>() {
                                @Override
                                public int compare(Task obj1, Task obj2) {
                                    if (obj1.getPriority() < obj2.getPriority()) return -1;
                                    // if (obj1.getPriority() == obj2.getPriority()) {
                                    //   if (obj1.getStart() < obj2.getStart()) return 1;

                                    //}
                                    return 1;
                                }
                            });*/

                    }
                }
            } else {
               // System.out.println(task.getId() + "primu 3");
              //  System.out.println("3" + " " + task.getId());
                  synchronized (this) {
                      queue.add(0, task);
                     /* if (queue.size() > 1)
                          queue.sort(new Comparator<Task>() {
                              @Override
                              public int compare(Task obj1, Task obj2) {
                                  if (obj1.getPriority() < obj2.getPriority()) return -1;
                                 // if (obj1.getPriority() == obj2.getPriority()) {
                                   //   if (obj1.getStart() < obj2.getStart()) return 1;

                                  //}
                                  return 1;
                              }
                          });*/
                  }

                }

        }
    }

    @Override
    public int getQueueSize() {
        int i = 0;
        if (currentTask != null) i = 1;
        return queue.size() + i;
    }

    @Override
    public long getWorkLeft() {
        long workLeft = 0;
        synchronized(this){
        for (Task e : queue) {
            workLeft += e.getLeft();
        }
        }
        if (currentTask != null) {
            workLeft += currentTask.getLeft() - ((long) ((Timer.getTimeDouble() - currentTaskTimer) * 1000));
        }
        return workLeft;
    }

    @Override
    public void shutdown() {
       // System.out.println("se da shutdown");
        currentTask = null;
       // currentTaskTimer = Timer.getTimeDouble();
        if(getWorkLeft() == 0)
        this.interrupt();
    }
}
