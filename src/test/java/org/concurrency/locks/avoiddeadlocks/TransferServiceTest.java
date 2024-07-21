package org.concurrency.locks.avoiddeadlocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance (TestInstance.Lifecycle.PER_CLASS)
public class TransferServiceTest
{

    private BankAccount account1;
    private BankAccount account2;

    @BeforeEach
    public void setUp()
    {
        account1 = new BankAccount(BigDecimal.valueOf(1000));
        account2 = new BankAccount(BigDecimal.valueOf(1000));
    }

    private static Stream<TransferService> transferServiceProvider()
    {
        return Stream.of(new InefficientTransferService(),
                new OrderingLocksTransferService(),
                new ReentrantLocksTransferService());
    }

    @ParameterizedTest
    @MethodSource ("transferServiceProvider")
    public void testSuccessfulTransfer(TransferService transferService)
    {
        transferService.transfer(account1, account2, BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(500), account1.getBalance());
        assertEquals(BigDecimal.valueOf(1500), account2.getBalance());
    }

    @ParameterizedTest
    @MethodSource ("transferServiceProvider")
    public void testInsufficientFunds(TransferService transferService)
    {
        assertThrows(IllegalArgumentException.class, () ->
        {
            transferService.transfer(account1, account2, BigDecimal.valueOf(1500));
        });
    }

    @ParameterizedTest
    @MethodSource ("transferServiceProvider")
    public void testTransferToSameAccount(TransferService transferService)
    {
        assertThrows(IllegalArgumentException.class, () ->
        {
            transferService.transfer(account1, account1, BigDecimal.valueOf(500));
        });
    }

    @ParameterizedTest
    @MethodSource ("transferServiceProvider")
    public void testConcurrentTransfers(TransferService transferService) throws InterruptedException
    {
        ExecutorService executor = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 100000; i++)
        {
            executor.submit(() ->
            {
                transferService.transfer(account1, account2, BigDecimal.valueOf(10));
                transferService.transfer(account2, account1, BigDecimal.valueOf(10));
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(BigDecimal.valueOf(1000), account1.getBalance());
        assertEquals(BigDecimal.valueOf(1000), account2.getBalance());
    }
}
