INSERT INTO users (username, password, role, enabled, name, address, email) VALUES
('bendegroot', 'thebestpassword', 2, true, 'Ben de Groot', 'Dahliastraat 5, Katwijk', 'bendegroot@yourcatsitter.nl'),
('jessicabosman', 'wachtwoord', 0, true, 'Jessica Bosman', 'Schoolstraat 3, Moddergat', 'jessica_bosman@hotmail.com'),
('willemjansen', '1234abc', 0, true, 'Willem Jansen', 'Beukenlaan 30, Lutjebroek', 'willem.jansen@ziggo.com'),
('pietjepuk', 'qwerty', 0, true, 'Pietje Puk', 'Straatweg 231, Bakkeveen', 'pietjepuk@gmail.com'),
('karelappel', 'Cats4Ever', 1, true, 'Karel Appel', 'Kerkstraat 44, Lopik', 'k.appel@gmail.com'),
('liesjepeer', 'GimmeAllTheCats', 1, true, 'Liesje Peer', 'Julianastraat 11, Boerenhol', 'l.peer@gmail.com'),
('annagulden', 'ILoveFirsa', 1, true, 'Anna Gulden', 'Lindelaan 3b, Nergenshuizen', 'annagulden@kpn.com');

INSERT INTO customers (username, password, name, address, email) VALUES
('jessicabosman', 'wachtwoord', 'Jessica Bosman', 'Schoolstraat 3, Moddergat', 'jessica_bosman@hotmail.com'),
('willemjansen', '1234abc', 'Willem Jansen', 'Beukenlaan 30, Lutjebroek', 'willem.jansen@ziggo.com'),
('pietjepuk', 'qwerty', 'Pietje Puk', 'Straatweg 231, Bakkeveen', 'pietjepuk@gmail.com');

INSERT INTO catsitters (username, password, name, address, email, about) VALUES
('karelappel', 'Cats4Ever', 'Karel Appel', 'Kerkstraat 44, Lopik', 'k.appel@gmail.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'),
('liesjepeer', 'GimmeAllTheCats', 'Liesje Peer', 'Julianastraat 11, Boerenhol', 'l.peer@gmail.com', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'),
('annagulden', 'ILoveFirsa', 'Anna Gulden', 'Lindelaan 3b, Nergenshuizen', 'annagulden@kpn.com', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.');

INSERT INTO orders (order_no, start_date, end_date, daily_number_of_visits, total_number_of_visits, customer_username, catsitter_username) VALUES
('7639b001-810e-446f-bd24-1bed57cacf7f', '2024-04-30', '2024-05-02', 1, 3, 'jessicabosman', 'annagulden'),
('6891e59c-8c9e-4784-a043-23dd4898be6a', '2023-07-23', '2023-08-06', 1, 14, 'jessicabosman', 'liesjepeer'),
('6e337a29-f104-4320-b6e5-7d061e1772d3', '2023-10-09', '2023-10-13', 2, 10, 'willemjansen', 'liesjepeer'),
('36639513-0a84-4ce5-a139-b356f03bfd2e', '2024-06-03', '2024-06-10', 2, 7, 'willemjansen', 'karelappel'),
('510acb89-c1da-487b-b03a-52afdae7e0c6', '2023-03-20', '2024-03-25', 1, 5,  'pietjepuk', 'karelappel'),
('be7e95b4-bb87-40dd-a4a7-c47d6c7673d9', '2024-07-01', '2024-07-21', 1, 21, 'pietjepuk', 'annagulden');

INSERT INTO cats (id, name, date_of_birth, gender, breed, general_info, spayed_or_neutered, vaccinated, veterinarian_name, phone_vet, medication_name, medication_dose, owner_username) VALUES
('1bdc5c0f-b821-4d98-a5f6-4296218244f5', 'Firsa', '2006-07-01', 'V', 'Europese Korthaar', 'Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?', true, true, 'Dierenkliniek Moddergat', '0123-456789', 'Semintra', '2ml 1dd', 'jessicabosman'),
('86be1447-dd98-4753-ba73-f8279e9c69c6', 'Poekie', '2020-04-30', 'V', 'Siamees', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', true, true, 'Dierenkliniek Lutjebroek', '0345-678901', 'geen', 'geen', 'willemjansen'),
('3af96ce7-783e-4127-b576-5d50ac555457', 'Pluis', '2014-10-14', 'M', 'Maine Coon', 'Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.', true, true, 'Dierenkliniek Lutjebroek', '0345-678901', 'Thiafeline', '3mg 2dd', 'willemjansen'),
('2faa27be-464d-415c-ba25-6ae531df136d', 'Pinkie', '2024-01-23', 'M', 'Europese Korthaar', 'Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.', false, false, 'Dierenkliniek Lutjebroek', '0345-678901', 'geen', 'geen', 'willemjansen'),
('40622485-28fe-4716-bfdb-d9d950ec67ba', 'Duchess', '2021-03-25', 'V', 'Pers', 'Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?', false, true, 'Dierenkliniek Bakkeveen', '0891-234567', 'geen', 'geen', 'pietjepuk'),
('888156d8-6d70-48f2-b50d-95bc3edc1cfc', 'Duke', '2022-02-11', 'M', 'Pers', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', false, true, 'Dierenkliniek Bakkeveen', '0891-234567', 'geen', 'geen', 'pietjepuk');

INSERT INTO invoices (invoice_no, invoice_date, amount, paid, order_no) VALUES
('7590075d-1a4c-4226-9157-4e97ec75a6ba', '2024-05-06', 42.0, false, '7639b001-810e-446f-bd24-1bed57cacf7f'),
('088e2c99-1318-44a7-86c7-86e8d331a9e6', '2023-08-10', 196.0, true, '6891e59c-8c9e-4784-a043-23dd4898be6a'),
('be2e0085-d7c0-45d1-afca-e910845ddb87', '2023-10-15', 210.0, true, '6e337a29-f104-4320-b6e5-7d061e1772d3'),
('1980d99d-1554-45e1-bdc2-8eb17364dae1', '2024-06-10', 294.0, false, '36639513-0a84-4ce5-a139-b356f03bfd2e'),
('da8afdb4-1dfa-49d0-b5d3-8f667b50f76c', '2024-03-27', 75.0, true, '510acb89-c1da-487b-b03a-52afdae7e0c6'),
('c8afe87f-1aa9-4f8b-a74d-f9b6f6ab463b', '2024-07-22', 315.0, false, 'be7e95b4-bb87-40dd-a4a7-c47d6c7673d9');

INSERT INTO tasks (task_no, task_type, task_instruction, extra_instructions, price_of_task, order_no) VALUES
('3a250773-09fe-474f-aa09-e5e1ed8220a5', 0, 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. ', 3.0, '7639b001-810e-446f-bd24-1bed57cacf7f'),
('451058e3-1698-47b7-b46f-f3c56d8b1229', 1, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.', 2.0, '7639b001-810e-446f-bd24-1bed57cacf7f'),
('5ffb173d-896b-4b3a-bea4-3232d742bf88', 2, 'Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.', 'Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.', 4.0, '7639b001-810e-446f-bd24-1bed57cacf7f'),
('9ab2ec7a-3700-4e6c-9f0b-123b0d54fe21', 3, 'Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?', 'Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?', 5.0, '7639b001-810e-446f-bd24-1bed57cacf7f'),
('35e41bfe-ac5f-4cdf-bd14-1db051f5233a', 0, 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. ', 3.0, '6891e59c-8c9e-4784-a043-23dd4898be6a'),
('6ad39f34-6063-49dc-a095-f25431199a7e', 1, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.', 2.0, '6891e59c-8c9e-4784-a043-23dd4898be6a'),
('5a8694a3-9760-4018-ab41-840202368a71', 2, 'Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.', 'Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.', 4.0, '6891e59c-8c9e-4784-a043-23dd4898be6a'),
('84efc841-bf59-4983-a049-3f140c529fd4', 3, 'Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?', 'Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?', 5.0, '6891e59c-8c9e-4784-a043-23dd4898be6a'),
('e41c64ac-32c5-4b43-8556-218678da1896', 0, 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. ', 3.0, '6e337a29-f104-4320-b6e5-7d061e1772d3'),
('c77c3936-d526-465e-99aa-072ed763e4a8', 1, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.', 2.0, '6e337a29-f104-4320-b6e5-7d061e1772d3'),
('298a2e1b-32a5-45ee-b8ea-57a60777de80', 2, 'Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.', 'Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.', 4.0, '6e337a29-f104-4320-b6e5-7d061e1772d3'),
('d7ba72f2-79d8-425e-8082-48a8d8d83c5f', 3, 'Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?', 'Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?', 5.0, '6e337a29-f104-4320-b6e5-7d061e1772d3'),
('04db96f8-1f62-4163-a0a2-c0583e42fcf4', 4, 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', 6.0, '6e337a29-f104-4320-b6e5-7d061e1772d3'),
('d186dbfa-5176-4a1c-a3c9-1a3e77b440c5', 5, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.', 1.0, '6e337a29-f104-4320-b6e5-7d061e1772d3'),
('515fc667-24b0-45e6-97de-1683961498e1', 0, 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', 3.0, '36639513-0a84-4ce5-a139-b356f03bfd2e'),
('9ca80366-a5d0-4d99-8600-5715e3948b58', 1, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.', 2.0, '36639513-0a84-4ce5-a139-b356f03bfd2e'),
('fcad40ee-8031-4ef6-9095-57b717425c56', 2, 'Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.', 'Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.', 4.0, '36639513-0a84-4ce5-a139-b356f03bfd2e'),
('dea20ce7-18f6-4aee-8819-466cc05f35e7', 3, 'Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?', 'Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?', 5.0, '36639513-0a84-4ce5-a139-b356f03bfd2e'),
('d783e388-92a0-423d-aa82-1790f55786c0', 4, 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', 6.0, '36639513-0a84-4ce5-a139-b356f03bfd2e'),
('c23d6067-d6bc-41b9-baf7-374e2aa1ca02', 5, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.', 1.0, '36639513-0a84-4ce5-a139-b356f03bfd2e'),
('875a4eab-aae3-4669-932a-5ed82feae861', 0, 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. ', 3.0, '510acb89-c1da-487b-b03a-52afdae7e0c6'),
('caa76d0a-b900-406f-a1c7-cbc6a0a8c1f1', 1, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.', 2.0, '510acb89-c1da-487b-b03a-52afdae7e0c6'),
('6f79a9b2-ae22-432f-91da-a5e5fec284fb', 2, 'Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.', 'Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.', 4.0, '510acb89-c1da-487b-b03a-52afdae7e0c6'),
('1f75b504-78e0-47d5-b9ee-fc90e0008104', 4, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.', 1.0, '510acb89-c1da-487b-b03a-52afdae7e0c6'),
('4674a3fd-8b37-4d82-b5a6-2b1668aef56f', 6, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.', 5.0, '510acb89-c1da-487b-b03a-52afdae7e0c6'),
('6b2b115c-ba42-4f92-aa61-221173f953ed', 0, 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', 3.0, 'be7e95b4-bb87-40dd-a4a7-c47d6c7673d9'),
('a34ea5ed-234f-4154-8773-f107808cc4d0', 1, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.', 2.0, 'be7e95b4-bb87-40dd-a4a7-c47d6c7673d9'),
('eb9d623b-4bd2-4c09-809e-49c689ccc1ac', 2, 'Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.', 'Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.', 4.0, 'be7e95b4-bb87-40dd-a4a7-c47d6c7673d9'),
('1a62d93f-253e-4b9c-97b8-346013dbd927', 4, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.', 1.0, 'be7e95b4-bb87-40dd-a4a7-c47d6c7673d9'),
('12f463ff-edd1-4748-8734-7d9b0755d95f', 6, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.', 5.0, 'be7e95b4-bb87-40dd-a4a7-c47d6c7673d9');
