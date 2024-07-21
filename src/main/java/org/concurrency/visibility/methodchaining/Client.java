package org.concurrency.visibility.methodchaining;

public class Client
{
    private volatile Person person;

    public Client()
    {
        Runnable runnable1 = () ->
        {
            person = Person.Builder
                    .newInstance()
                    .setName("joe")
                    .setSalary(19000d)
                    .build();

        };

        Runnable runnable2 = () ->
        {
            person = Person.Builder
                    .newInstance()
                    .setName("Philip")
                    .setSalary(20000d)
                    .build();

        };

        Thread t1 = new Thread(runnable1);
        Thread t2 = new Thread(runnable2);
        t1.start();
        t2.start();
        try
        {
            t1.join();
            t2.join();
        }
        catch (InterruptedException ex)
        {
            System.out.println("Object creation failure");
        }
    }

    public Person getPerson()
    {
        return person;
    }

    public static void main(String[] args)
    {
        Client client = new Client();

        System.out.println(client.getPerson().getName());
        System.out.println(client.getPerson().getSalary());
    }
}
