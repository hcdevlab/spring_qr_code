CREATE TABLE qr_control
(
    id_qr SERIAL,
    name_user VARCHAR(100) NOT NULL,
    machine_qr_code VARCHAR(300) NOT NULL,
    date_hour VARCHAR(100) NOT NULL,
    lat_coord VARCHAR(100) NOT NULL,
    long_coord VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_qr)
);