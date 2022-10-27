/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Adnaane
 * Created: Oct 25, 2022
 */

INSERT INTO DEALER (id, name, tier_limit) VALUES ('983752b2-5505-11ed-bdc3-0242ac120002', 'John', 3);
INSERT INTO DEALER (id, name, tier_limit) VALUES ('b302b320-5505-11ed-bdc3-0242ac120002', 'Albert', 3);
INSERT INTO DEALER (id, name, tier_limit) VALUES ('b96061b8-5505-11ed-bdc3-0242ac120002', 'Paul', 3);
INSERT INTO DEALER (id, name, tier_limit) VALUES ('bd985876-5505-11ed-bdc3-0242ac120002', 'Pierre', 3);
INSERT INTO DEALER (id, name, tier_limit) VALUES ('c3cd7df2-5505-11ed-bdc3-0242ac120002', 'Arnold', 1);


INSERT INTO LISTING (id, dealer_Id, vehicle, price, created_at, state ) 
VALUES ('84e3db9e-5506-11ed-bdc3-0242ac120002','c3cd7df2-5505-11ed-bdc3-0242ac120002', 'BMV', 10.5, '10/10/2022', 'published');

  