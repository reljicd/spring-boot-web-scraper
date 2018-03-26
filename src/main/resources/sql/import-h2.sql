ALTER TABLE LINK
  ALTER COLUMN create_date SET DEFAULT CURRENT_TIMESTAMP;

-- Users
-- password in plaintext: "password"
INSERT INTO USER (user_id, password, email, username, name, last_name, active)
VALUES
  (1, '$2a$06$OAPObzhRdRXBCbk7Hj/ot.jY3zPwR8n7/mfLtKIgTzdJa4.6TwsIm', 'user@mail.com', 'user', 'User', 'R',   1);
-- password in plaintext: "password"
INSERT INTO USER (user_id, password, email, username, name, last_name, active)
VALUES
  (2, '$2a$06$OAPObzhRdRXBCbk7Hj/ot.jY3zPwR8n7/mfLtKIgTzdJa4.6TwsIm', 'johndoe@gmail.com', 'johndoe', 'John', 'Doe', 1);

-- Roles
INSERT INTO ROLE (role_id, role)
VALUES (1, 'ROLE_ADMIN');
INSERT INTO ROLE (role_id, role)
VALUES (2, 'ROLE_USER');

-- User Roles
INSERT INTO USER_ROLE (user_id, role_id)
VALUES (1, 1);
INSERT INTO USER_ROLE (user_id, role_id)
VALUES (1, 2);
INSERT INTO USER_ROLE (user_id, role_id)
VALUES (2, 2);

-- Links
INSERT INTO LINK (link_id, user_id, url, create_date)
VALUES (1, 1, 'http://www.bbc.com/news/magazine-38160671',
        CURRENT_TIMESTAMP());
INSERT INTO LINK (link_id, user_id, url, create_date)
VALUES (2, 1, 'http://www.bbc.com/news/magazine-38160671',
        CURRENT_TIMESTAMP());
INSERT INTO LINK (link_id, user_id, url, create_date)
VALUES (3, 1, 'http://www.bbc.co.uk/guides/zg3hk7h',
        CURRENT_TIMESTAMP());
INSERT INTO LINK (link_id, user_id, url, create_date)
VALUES (4, 1, 'http://www.bbc.com/future/story/20161208-why-vitamin-supplements-could-kill-you',
        CURRENT_TIMESTAMP());
INSERT INTO LINK (link_id, user_id, url, create_date)
VALUES (5, 1, 'http://www.bbc.co.uk/sport',
        CURRENT_TIMESTAMP());
INSERT INTO LINK (link_id, user_id, url, create_date)
VALUES (6, 2, 'http://www.bbc.com/sport/basketball/40009924',
        CURRENT_TIMESTAMP());
INSERT INTO LINK (link_id, user_id, url, create_date)
VALUES (7, 2, 'http://www.bbc.com/sport/basketball/40009924',
        CURRENT_TIMESTAMP());

-- Tags
INSERT INTO TAG (tag_id, tag)
VALUES (1, 'news');
INSERT INTO TAG (tag_id, tag)
VALUES (2, 'sports');
INSERT INTO TAG (tag_id, tag)
VALUES (3, 'sport');
INSERT INTO TAG (tag_id, tag)
VALUES (4, 'old');
INSERT INTO TAG (tag_id, tag)
VALUES (5, 'it');

-- Links Tags
INSERT INTO LINK_TAG (link_id, tag_id)
VALUES (1, 1);
INSERT INTO LINK_TAG (link_id, tag_id)
VALUES (1, 2);
INSERT INTO LINK_TAG (link_id, tag_id)
VALUES (1, 3);
INSERT INTO LINK_TAG (link_id, tag_id)
VALUES (2, 2);
INSERT INTO LINK_TAG (link_id, tag_id)
VALUES (3, 5);
INSERT INTO LINK_TAG (link_id, tag_id)
VALUES (4, 1);
INSERT INTO LINK_TAG (link_id, tag_id)
VALUES (4, 2);
INSERT INTO LINK_TAG (link_id, tag_id)
VALUES (4, 3);
INSERT INTO LINK_TAG (link_id, tag_id)
VALUES (4, 5);
INSERT INTO LINK_TAG (link_id, tag_id)
VALUES (5, 1);
INSERT INTO LINK_TAG (link_id, tag_id)
VALUES (5, 2);
INSERT INTO LINK_TAG (link_id, tag_id)
VALUES (5, 3);