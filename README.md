
This repository contains examples and explanations of various Java concurrency practices to safeguard critical section.
The examples cover a wide range of topics, including visibility and atomicity, locks, thread pools, double-check locking pattern,
volatile variables, and more. Each concept is demonstrated with code examples and explanations to help you understand 
and apply these best practices in your own code.

    Topics covered in code examples

    Visibility and Atomicity
        Importance of visibility and atomicity in concurrent programming.
        Use of volatile and atomic classes (AtomicInteger, AtomicLong, etc.) to ensure visibility and atomic operations.

    Locks
        Using synchronized methods and blocks to ensure mutual exclusion.
        Using ReentrantLock for more advanced locking mechanisms.
        Private final locks to avoid unintended lock interactions.
        Lock with super classes and subclasses. Ensuring that overridden methods in subclasses are also thread-safe.
        Do not synchronize on a colleciton view
        synchronized static methods with static locks
    
    Avoiding Deadlocks
        Techniques to avoid deadlocks:
        Ordering locks: Always acquire locks in a consistent order.
        Using tryLock: Attempt to acquire a lock and back off if unable to do so.
        Backoff with exponential retry: Implementing exponential backoff to reduce contention.
        Holding and releasing locks in the same order to avoid circular dependencies.

    Thread Pools
        Using ExecutorService and its implementations (FixedThreadPool, CachedThreadPool, etc.) for managing a pool of threads.
        Gracefully shutting down thread pools using shutdown and shutdownNow and submit tasks that can be interrupted
        Creating custom thread pools by extending ThreadPoolExecutor.
        Exception handling with thread pools using Thread factory and UncaughtExceptionHandler

    Graceful Shutdown
        Proper techniques to shut down thread pools and handle tasks that are in progress or pending.

    Custom Thread Pools
        Implementing custom thread pools that handle runtime exceptions using afterExecute and beforeExecute method in ThreadPoolExecutor.

    ThreadLocal Variables
        Using ThreadLocal to manage per-thread data, ensuring that each thread has its own instance of a variable.

    Double-Check Locking Pattern
        Implementing the double-check locking pattern to create singletons in a thread-safe manner without unnecessary synchronization.

    Synchronization on Collection Views
        Avoiding synchronization on collection views and instead synchronizing on the backing collection to prevent inconsistencies.

    Volatile Variables
        Using volatile to ensure visibility of changes to variables across threads.

    Publishing Fully Initialized Objects
        Ensuring that objects are fully initialized before they are made visible to other threads.
        Using final and volatile fields to guarantee visibility of the fully constructed object.

    Interrupts
        Proper handling of interrupts in threads to ensure that threads can terminate gracefully.

    Exception Handling and Locks
        Ensuring that locks are released in finally blocks to avoid leaving a lock in a locked state if an exception is thrown.

    Finalizer Attacks
        Avoiding finalizer attacks by not allowing constructors to throw exceptions that might leave an object in a partially constructed state.

    Inner Classes
        Using static inner classes to avoid holding implicit references to the outer class and preventing memory leaks.

    Avoid Passing Internal State to Alien Methods
        Avoiding passing internal state to alien methods to prevent those methods from retaining references to the internal state and potentially causing inconsistencies or security issues.
