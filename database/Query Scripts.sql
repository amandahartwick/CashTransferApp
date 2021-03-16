BEGIN TRANSACTION;

UPDATE transfers SET transfer_status_id = 2  WHERE transfer_id = 15;

ROLLBACK;