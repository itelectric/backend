ALTER TABLE t_service ADD COLUMN estimated_time_seconds BIGINT;

UPDATE t_service
SET estimated_time_seconds = EXTRACT(EPOCH FROM estimated_time);

ALTER TABLE t_service DROP COLUMN estimated_time;

ALTER TABLE t_service RENAME COLUMN estimated_time_seconds TO estimated_time;
