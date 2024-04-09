-- NB: dit werkt nog niet

INSERT INTO users (username, password, role, authorities, enabled, name, address, email) VALUES
("bendegroot", "thebestpassword", "ADMIN", null, true, "Ben de Groot", "Dahliastraat 5, Katwijk", "bendegroot@yourcatsitter.nl"),
("marjetbosma", "wachtwoord", "CUSTOMER", null, true, "Marjet Bosma", "Schoolstraat 3, Moddergat", "marjet_bosma@hotmail.com"),
("willemhustinx" "1234abc", "CUSTOMER", null, true, "Willem Hustinx", "Beukenlaan 30, Lutjebroek", "willemhustinx@sogeti.com"),
("pietjepuk", "qwerty", "CUSTOMER", null, true, "Pietje Puk", "Straatweg 231, Bakkeveen", "pietje.puk@gmail.com"),
("karelappel", "Cats4Ever", "CATSITTER", null, true, "Karel Appel", "Kerkstraat 44, Lopik", "k.appel@gmail.com"),
("liesjepeer", "GimmeAllTheCats", "CATSITTER", null, true, "Liesje Peer", "Julianastraat 11, Boerenhol", "l.peer@gmail.com"),
("hannahdaalder", "ILoveFirsa", "CATSITTER", null, true, "Hannah Daalder", "Lindelaan 3b, Nergenshuizen", "hannahdaalder@kpn.com")
-- password encryption, authorities

INSERT INTO customers (username, password, name, address, email) VALUES
("marjetbosma", "wachtwoord", "Marjet Bosma", "Schoolstraat 3, Moddergat", "marjet_bosma@hotmail.com"),
("willemhustinx" "1234abc", "Willem Hustinx", "Beukenlaan 30, Lutjebroek", "willemhustinx@sogeti.com"),
("pietjepuk", "qwerty", "Pietje Puk", "Straatweg 231, Bakkeveen", "pietje.puk@gmail.com")
-- password encryption

INSERT INTO catsitters (username, password, name, address, email, about) VALUES
("karelappel", "Cats4Ever", "Karel Appel", "Kerkstraat 44, Lopik", "k.appel@gmail.com", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."),
("liesjepeer", "GimmeAllTheCats", "Liesje Peer", "Julianastraat 11, Boerenhol", "l.peer@gmail.com", "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"),
("hannahdaalder", "ILoveFirsa", "Hannah Daalder", "Lindelaan 3b, Nergenshuizen", "hannahdaalder@kpn.com", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.")
-- password encryption

INSERT INTO cats (id, name, dateOfBirth, gender, breed, generalInfo, spayedOrNeutered, vaccinated, veterinarianName, phoneVet, medicationName, medicationDose, ownerUsername) VALUES
("1bdc5c0f-b821-4d98-a5f6-4296218244f5", "Firsa", "2006-07-01", "V", "Europese Korthaar", "Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?", true, true, "Dierenkliniek Moddergat", "0123-456789", "Semintra", "2ml 1dd", "marjetbosma"),
("86be1447-dd98-4753-ba73-f8279e9c69c6", "Poekie", "2020-30-04", "V", "Siamees", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", true, true, "Dierenkliniek Lutjebroek",  "geen", "geen", "willemhustinx"),
("3af96ce7-783e-4127-b576-5d50ac555457", "Pluis", "2014-14-10", "M", "Maine Coon", "Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.", true, true, "Dierenkliniek Lutjebroek", "0345-678901", "Thiafeline", "3mg 2dd", "willemhustinx"),
("2faa27be-464d-415c-ba25-6ae531df136d", "Pinkie", "2024-01-23", "M", "Europese Korthaar", "Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.", false, false, "Dierenkliniek Lutjebroek", "0345-678901", "geen", "geen", "willemhustinx"),
("40622485-28fe-4716-bfdb-d9d950ec67ba", "Duchess", "2021-03-25", "V", "Pers", "Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?", false, true, "Dierenkliniek Bakkeveen", "0891-234567", "geen", "geen", "pietjepuk"),
("888156d8-6d70-48f2-b50d-95bc3edc1cfc", "Duke", "2022-01-03", "M", "Pers", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." false, true, "Dierenkliniek Bakkeveen", "0891-234567", "geen", "geen", "pietjepuk",)

INSERT INTO orders (orderNo, startDate, endDate, dailyNumberOfVisits, totalNumberOfVisits, customerUsername, catsitterUsername) VALUES
("7639b001-810e-446f-bd24-1bed57cacf7f", "2024-04-30", "2024-05-02", 1, 3, "marjetbosma", "hannahdaalder"),


INSERT INTO tasks (taskNo, taskType, taskInstruction, extraInstructions, priceOfTask, orderNo) VALUES
("3a250773-09fe-474f-aa09-e5e1ed8220a5", "FOOD", "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. ", 3.0, "7639b001-810e-446f-bd24-1bed57cacf7f"),
("7639b001-810e-446f-bd24-1bed57cacf7f", "WATER", "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.", 2.0, "7639b001-810e-446f-bd24-1bed57cacf7f"),
("5ffb173d-896b-4b3a-bea4-3232d742bf88", "LITTERBOX", "Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.", "Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.", 4.0, "7639b001-810e-446f-bd24-1bed57cacf7f"),
("9ab2ec7a-3700-4e6c-9f0b-123b0d54fe21", "MEDICATION", "Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?", "Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?", 5.0, "7639b001-810e-446f-bd24-1bed57cacf7f")

INSERT INTO invoices (invoiceNo, invoiceDate, amount, paid, orderNo) VALUES
("7590075d-1a4c-4226-9157-4e97ec75a6ba", "2024-05-06", 42.0, false, "7639b001-810e-446f-bd24-1bed57cacf7f")

