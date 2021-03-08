SELECT * FROM accounts WHERE account_id = 1;

INSERT INTO transfers (transfer_id, transfer_status_id, transfer_type_id, account_from, account_to, amount)
VALUES (?, 2, 2, ?, ?, ?);

UPDATE accounts SET balance = ? WHERE account_id = ?;