ALTER TABLE payments
DROP CONSTRAINT payments_payment_status_check;

ALTER TABLE payments
    ADD CONSTRAINT payments_payment_status_check
        CHECK (
            payment_status IN (
                               'CREATED',
                               'PENDING',
                               'PAID',
                               'OVERDUE',
                               'FAILED',
                               'REFUNDED',
                               'REFUND_PENDING',
                               'CANCELLED'
                )
            );