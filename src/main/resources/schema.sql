CREATE TABLE bookings (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          room VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL,
                          date DATE NOT NULL,
                          time_from TIME NOT NULL,
                          time_to TIME NOT NULL
);
