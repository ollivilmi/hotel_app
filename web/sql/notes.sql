SELECT n.id AS id, n.contents AS contents, n.note_date AS note_date, n.department_id AS department_id, n.img_url AS img_url, nr.user_id AS user_id 
                FROM Notes n, Note_Receivers nr 
                WHERE n.id = nr.note_id AND (user_id = 4 OR department_id = 1) ORDER BY note_date;