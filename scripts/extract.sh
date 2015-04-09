#!/bin/bash -x

mkdir ../data

output_dir='../data'
#Get all of a directory(sdcard), and filter them
for file in `adb shell ls /sdcard/trepn/*.db`
do
    file=`echo -e $file | tr -d "\r\n"` # osx fix! ghhrrr :@ :(
    # pull the command
    adb pull $file $output_dir
    adb shell "rm $file"
done

mkdir ../data/backup
backup_dir='../data/backup'
cd $output_dir
for db in *.db;
do
    touch $db.csv
    sqlite3 $db "Select name from sqlite_master where name like 'sensor_%';" |
    while read -r line;
    do
        bash ../scripts/sql.sh $db $line

        #append sensor file to full output file
        cat $db-$line.csv >> $db.csv

        #cleanup sensor file since all we want is a single comprehensive file for the db
        rm $db-$line.csv
    done;

    mv $db $backup_dir
    rm $db-shm
    rm $db-wal
done
echo "-- extraction complete --"