package org.concurrency.visibility.methodchaining;

public final class Person
{
    private final String name;
    private final int age;
    private final String gender;
    private final String address;
    private final double salary;

    public Person(final Builder builder)
    {
        this.name = builder.name;
        this.age = builder.age;
        this.gender = builder.gender;
        this.address = builder.address;
        this.salary = builder.salary;
    }

    public String getName()
    {
        return name;
    }

    public int getAge()
    {
        return age;
    }

    public String getGender()
    {
        return gender;
    }

    public String getAddress()
    {
        return address;
    }

    public double getSalary()
    {
        return salary;
    }

    public static class Builder
    {
        private String name;
        private int age;
        private String gender;
        private String address;
        private double salary;

        private Builder()
        {

        }

        public static Builder newInstance()
        {
            return new Builder();
        }

        public Builder setName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder setAge(int age)
        {
            this.age = age;
            return this;
        }

        public Builder setGender(String gender)
        {
            this.gender = gender;
            return this;
        }

        public Builder setAddress(String address)
        {
            this.address = address;
            return this;
        }

        public Builder setSalary(double salary)
        {
            this.salary = salary;
            return this;
        }

        public Person build()
        {
            return new Person(this);
        }
    }
}
