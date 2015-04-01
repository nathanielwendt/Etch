#1 db_name
#2 sensor_name

sqlite3 $1 <<!
.separator ","
.mode csv
.output $1-$2.csv
select '$2', timestamp, value, delta, end_time-start_time, description from

$2,
(select st_st as end_time, et_st as start_time, description
from ((select _id AS st_id,start_time AS st_st, description from application_state where (_id%2) = 0)),
((select _id + 1 AS et_id,start_time AS et_st from application_state where (_id % 2) <> 0))
WHERE st_id=et_id)


WHERE start_time <= $2.timestamp
AND end_time >= $2.timestamp;
!