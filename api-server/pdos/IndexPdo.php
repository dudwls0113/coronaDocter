<?php
require './pdos/MoonPdo.php';

function emailcheckGuest($email)
{
    $pdo = pdoSqlConnect();
    $query = "select exists(select * from users where email = ?)as exist;";

    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$email]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res[0]['exist'];
}

function nicknamecheckGuest($nickname)
{
    $pdo = pdoSqlConnect();
    $query = "select exists(select * from users where nickname = ?)as exist;";

    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$nickname]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res[0]['exist'];
}

function signUp($email, $name, $birth, $password, $nickname)
{
    try {
        $is_deleted = (string)'N';

        $pdo = pdoSqlConnect();
        $pdo->beginTransaction();

        $query = "INSERT INTO images (is_deleted, type) VALUES (?, 2);";
        $st = $pdo->prepare($query);
        $st->bindParam(1, $is_deleted, PDO::PARAM_STR);
        $st->execute();

        $query = "select no from images order by no desc;";

        $st = $pdo->prepare($query);
        //    $st->execute([$param,$param]);
        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        $image_no = $res[0]['no'];

        $no = (int)0;
        $email = (string)$email;
        $name = (string)$name;
        $birth = (string)$birth;
        $password = (string)$password;
        $auth = (string)"Y";
        $is_deleted = (int)0;
        $nickname = (string)$nickname;
        $timestamp = date("Y-m-d H:i:s");

        $query = "INSERT INTO users (no, email, name, birth, password, Authorization, registered_timestamp, is_deleted, profile_image_no, nickname) VALUES (?,?,?,?,?,?,?,?,?,?);";


        $st = $pdo->prepare($query);
        $st->bindParam(1, $no, PDO::PARAM_INT);
        $st->bindParam(2, $email, PDO::PARAM_STR);
        $st->bindParam(3, $name, PDO::PARAM_STR);
        $st->bindParam(4, $birth, PDO::PARAM_INT);
        $st->bindParam(5, $password, PDO::PARAM_STR);
        $st->bindParam(6, $auth, PDO::PARAM_STR);
        $st->bindParam(7, $timestamp, PDO::PARAM_STR);
        $st->bindParam(8, $is_deleted, PDO::PARAM_STR);
        $st->bindParam(9, $image_no, PDO::PARAM_INT);
        $st->bindParam(10, $nickname, PDO::PARAM_STR);
        $st->execute();

        $query = "select no from users order by no desc limit 1;";

        $st = $pdo->prepare($query);
        //    $st->execute([$param,$param]);
        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        $userNo = $res[0]['no'];


        $is_deleted = (string)'N';
        $is_registered = (string)date("Y-m-d H:i:s");

        $query = "insert into images (is_deleted, is_registered, type, user_no) values (?,?,3,?);";
        $st = $pdo->prepare($query);
        $st->bindValue(1, $is_deleted);
        $st->bindValue(2, $is_registered);
        $st->bindValue(3, $userNo);
        $st->execute();

        $scrapName = (string)'기본 스크랩북';
        $closure = (string)'Y';
        $query = "insert into scraps (no, title, title_image_no, registered_timestamp, is_deleted, user_no, is_closure) values (?,?,(select no from images order by no desc limit 1),?,?,?,?);";

        $st = $pdo->prepare($query);
        $st->execute([$no, $scrapName, $is_registered, $is_deleted, $userNo, $closure]);

        $pdo->commit();

    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
        return 2;
    }

}

function insertImage($url, $userNo, $type)
{

    $url = (string)$url;
    $no = (int)0;
    $is_deleted = (string)'N';

    $pdo = pdoSqlConnect();
    $query = "INSERT INTO images (no, is_deleted, url, user_no, type) VALUES (?,?,?,?,?);";
    $st = $pdo->prepare($query);
    $st->bindParam(1, $no, PDO::PARAM_INT);
    $st->bindParam(2, $is_deleted, PDO::PARAM_STR);
    $st->bindParam(3, $url, PDO::PARAM_STR);
    $st->bindParam(4, $userNo, PDO::PARAM_INT);
    $st->bindParam(5, $type, PDO::PARAM_INT);

    $st->execute();

    $st = null;
    $pdo = null;
}

function getArea()
{
    $pdo = pdoSqlConnect();
    $query = "select * from locations ";

    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute();
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}

function postArea($userNo, $national)
{
    try {
        $no = 0;
        $int = 0;
        $count = count($national);
        $question_marks = str_repeat(",(?,?,?)", $count - 1);

        $pdo = pdoSqlConnect();
        $query = " INSERT INTO interesting_relations (no , user_no, location_no) VALUES (?,?,?)$question_marks;";

        $st = $pdo->prepare($query);

        foreach ($national as $row => $value) {
            $st->bindValue($int + 1, $no);
            $st->bindValue($int + 2, $userNo);
            $st->bindValue($int + 3, $value);
            $int = $int + 3;
        }
        $st->execute();

        $st = null;
        $pdo = null;

    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
        return 2;
    }

}

function login($email, $password)
{
    try {
        $pdo = pdoSqlConnect();
        $pdo->beginTransaction();

        $query = "select exists(select * from users where email = ? and password = ?)as exist;";
        $st = $pdo->prepare($query);
        $st->execute([$email, $password]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        if ($res[0]['exist'] == 1) {
            $query2 = "UPDATE users SET isLogOut = 0 WHERE email = ?;";
            $st2 = $pdo->prepare($query2);
            $st2->execute([$email]);
        }

        $pdo->commit();

        $st = null;
        $pdo = null;

        return $res[0]['exist'];
    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
        return 2;
    }
}

function getUser($userNo)
{
    $is_deleted = 'N';
    $pdo = pdoSqlConnect();
    $query = "select url, nickname, email, introduce from users inner join images on  users.profile_image_no = images.no where users.No = ? and images.is_deleted = ?;";
//    echo "$query";
    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$userNo, $is_deleted]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}

function getMyarea($userNo)
{
    $pdo = pdoSqlConnect();
    $query = "select nationalNo, name from
(select * from locations) selectNational inner join
(select distinct location_no from users inner join interesting_relations on users.no = interesting_relations.user_no where users.no = ?) selectUser
on selectNational.nationalNo = selectUser.location_no order by nationalNo;";
//    echo "$query";
    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$userNo]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}

function viewdetailUser($userNo)
{
    $pdo = pdoSqlConnect();
    $query = "select url, nickname, email, introduce, Authorization from users inner join images on users.profile_image_no = images.no where users.no = ? group by users.no;";

    $st = $pdo->prepare($query);
    $st->execute([$userNo]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();
    $st = null;
    $pdo = null;

    $is_deleted = 'N';

    $pdo = pdoSqlConnect();
    $query = "select selectNational.nationalNo, name from
(select * from locations) selectNational inner join
(select distinct location_no from users inner join interesting_relations on users.no = interesting_relations.user_no where users.no = ? and interesting_relations.is_deleted = ?) selectUser
on selectNational.nationalNo = selectUser.location_no;";

    $st = $pdo->prepare($query);
    $st->execute([$userNo, $is_deleted]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res2 = $st->fetchAll();
    $st = null;
    $pdo = null;

    return array('user' => $res[0], 'national' => $res2);

}


function patchUser($url, $introduce, $userNo, $national) //관심지역 추가 요망
{
    try {
        $pdo = pdoSqlConnect();
        $queryIamge = "UPDATE images
                        SET url = ?, user_no = ?
                        WHERE NO = (select profile_image_no from users where no = ?);";
        $st1 = $pdo->prepare($queryIamge);


        $queryIntroduce = "UPDATE users
                        SET introduce = ?
                        WHERE NO = ?;";
        $st2 = $pdo->prepare($queryIntroduce);

        $queryDelete = "DELETE FROM interesting_relations WHERE user_no = ?;";
        $st3 = $pdo->prepare($queryDelete);

        $no = 0;
        $int = 0;
        $count = count($national);
        $question_marks = str_repeat(",(?,?,?)", $count - 1);

        $queryInterested = " INSERT INTO interesting_relations (no , user_no, location_no) VALUES (?,?,?)$question_marks;";
        $st4 = $pdo->prepare($queryInterested);
        foreach ($national as $row => $value) {
            $st4->bindValue($int + 1, $no);
            $st4->bindValue($int + 2, $userNo);
            $st4->bindValue($int + 3, $value);
            $int = $int + 3;
        }

        $pdo->beginTransaction();
        $st1->execute([$url, $userNo, $userNo]);
        $st2->execute([$introduce, $userNo]);
        $st3->execute([$userNo]);
        $st4->execute();

        $pdo->commit();


        $st = null;
        $pdo = null;

        return 1;
    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        throw $e;
        return 2;
    }
}

function without_postPost($userNo, $nationalNo, $text)
{
    $no = (int)0;
    $text = (string)$text;
    $nationalNo = (int)$nationalNo;
    $is_registered = (string)date("Y-m-d H:i:s");
    $is_deleted = (string)'N';

    $pdo = pdoSqlConnect();
    $query = "insert into posts (no, contents, user_no, location_no, registered_timestamp, is_deleted) values (?,?,?,?,?,?);";
//    echo "$query";
    $st = $pdo->prepare($query);
    $st->execute([$no, $text, $userNo, $nationalNo, $is_registered, $is_deleted]);
    $st = null;
    $pdo = null; //insert post
}


function postPost($userNo, $nationalNo, $text, $photoResult)
{
    try {
        $no = (int)0;
        $text = (string)$text;
        $nationalNo = (int)$nationalNo;
        $nowTime = (string)date("Y-m-d H:i:s");
        $is_deleted = (string)'N';

        $pdo = pdoSqlConnect();
        $pdo->beginTransaction();

        $query = "insert into posts (no, contents, user_no, location_no, registered_timestamp, is_deleted) values (?,?,?,?,?,?);";
        $st = $pdo->prepare($query);
        $st->execute([$no, $text, $userNo, $nationalNo, $nowTime, $is_deleted]);

        foreach ($photoResult as $row => $value) //이미지
        {
            $query = "insert into images (is_deleted, url, user_no, type, is_registered) values ('N',:url,:userNo,1, :now);";
            $st = $pdo->prepare($query);
            $st->bindValue(':url', $value, PDO::PARAM_INT);
            $st->bindValue(':userNo', $userNo, PDO::PARAM_INT);
            $st->bindValue(':now', $nowTime, PDO::PARAM_STR);

            $st->execute();
        }

        $query = "SELECT no as post_no FROM posts WHERE user_no = :userNo ORDER BY no DESC LIMIT 1;";
        $st = $pdo->prepare($query);
        $st->bindValue(':userNo', $userNo, PDO::PARAM_INT);
        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $postNo = $res[0]['post_no'];

        $query = "SELECT no as image_no FROM images WHERE user_no = :userNo ORDER BY no DESC LIMIT :limit;";
        $st = $pdo->prepare($query);
        $st->bindValue(':userNo', $userNo, PDO::PARAM_INT);
        $st->bindValue(':limit', count($photoResult), PDO::PARAM_INT);
        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        foreach ($res as $item) {
            $query = "insert into post_image_relations (post_image_no, post_no) values (:postImageNo, :postNo);";
            $st = $pdo->prepare($query);
            $result_image_no = $item['image_no'];
            $st->bindValue(':postImageNo', $result_image_no, PDO::PARAM_INT);
            $st->bindValue(':postNo', $postNo, PDO::PARAM_INT);
            $st->execute();
        }

        $pdo->commit();

    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            echo $e;
            $pdo->rollback();
        }
        return 2;
    }
}


function getPost($postNo, $userNo)
{
    try {

        $post_result = array();
        $int = 0;
        $postNo = (int)$postNo;
        $is_deleted = (string)'N';
        $pdo = pdoSqlConnect();
        $query = "select getlike.postNo,
             nickname,
             getlike.user_no           as userNo,
             profileUrl,
             getlike.registered_timestamp,
             contents,
             likedCount,
             count(scrap_relations.no) as scrapCount
      from (
               select getUser.postNo,
                      nickname,
                      getUser.user_no,
                      profileUrl,
                      registered_timestamp,
                      contents,
                      count(likes.no) as likedCount
               from (select postinUSER.no as postNo,
                            nickname,
                            postinUSER.user_no,
                            images.url    as profileUrl,
                            postinUSER.name,
                            postinUSER.registered_timestamp,
                            contents
                     from users
                              inner join images
                              inner join (select *
                                          from posts
                                                   inner join locations on posts.location_no = locations.nationalNo
                                          where posts.no = ?
                                            and is_deleted = ?) postinUSER
                                         on users.no = postinUSER.user_no and users.profile_image_no = images.no and
                                            postinUSER.user_no = users.no) getUser
                        left outer join likes
                                        on getUser.postNo = likes.post_no and likes.is_deleted = ?
               group by getUser.postNo, nickname, profileUrl,   registered_timestamp, contents) getlike
               left outer join scrap_relations
                               on getlike.postNo = scrap_relations.post_no and scrap_relations.is_deleted = ?
      group by getlike.postNo, nickname, profileUrl, registered_timestamp, contents, likedCount
      order by registered_timestamp";

        $st = $pdo->prepare($query);
        //    $st->execute([$param,$param]);

        $st = $pdo->prepare($query);
        $st->bindParam(1, $postNo, PDO::PARAM_INT);
        $st->bindParam(2, $is_deleted, PDO::PARAM_STR);
        $st->bindParam(3, $is_deleted, PDO::PARAM_STR);
        $st->bindParam(4, $is_deleted, PDO::PARAM_STR);
        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        $st = null;
        $pdo = null;

        foreach ($res as $row) {
            $postNo = $row['postNo'];

            $pdo2 = pdoSqlConnect();
            $querymyLike = "select exists(select * from posts inner join likes on posts.no = likes.post_no where post_no = ? and likes.is_deleted = ? and likes.user_no = ?) as myLike;";
            $st1 = $pdo2->prepare($querymyLike);
            $querymyScrap = "select exists(select * from posts inner join scrap_relations on posts.no = scrap_relations.post_no where post_no = ? and scrap_relations.is_deleted = ? and scrap_relations.user_no = ?) as myScrap;";
            $st2 = $pdo2->prepare($querymyScrap);
            $querymyPost = "select exists (select * from posts inner join users on posts.user_no = users.no where posts.no = ? and posts.user_no = ? )as myPost;";
            $st3 = $pdo2->prepare($querymyPost);
            $queryimageUrl = "select distinct url as imageUrl from posts inner join post_image_relations inner join images WHERE posts.no = post_image_relations.post_no AND post_image_relations.post_image_no = images.no AND posts.no =? and images.is_deleted = ?;";
            $st4 = $pdo2->prepare($queryimageUrl);
            $querycomments = "select count(*)as commentsCount from posts inner join comments on comments.post_no = posts.no where posts.no = ? and comments.is_deleted = ? order by comments.comment_sequence;";
            $st5 = $pdo2->prepare($querycomments);
            $queryNational = "select locations.name as nationalName from posts inner join locations on posts.location_no = locations.nationalNo where posts.no = ?;";
            $st6 = $pdo2->prepare($queryNational);

            $pdo2->beginTransaction();
            $st1->execute([$postNo, $is_deleted, $userNo]);
            $st2->execute([$postNo, $is_deleted, $userNo]);
            $st3->execute([$postNo, $userNo]);
            $st4->execute([$postNo, $is_deleted]);
            $st5->execute([$postNo, $is_deleted]);
            $st6->execute([$postNo]);
            $pdo2->commit();

            $st1->setFetchMode(PDO::FETCH_ASSOC);
            $res1 = $st1->fetchAll();
            $st2->setFetchMode(PDO::FETCH_ASSOC);
            $res2 = $st2->fetchAll();
            $st3->setFetchMode(PDO::FETCH_ASSOC);
            $res3 = $st3->fetchAll();
            $st4->setFetchMode(PDO::FETCH_ASSOC);
            $res4 = $st4->fetchAll();
            $st5->setFetchMode(PDO::FETCH_ASSOC);
            $res5 = $st5->fetchAll();
            $st6->setFetchMode(PDO::FETCH_ASSOC);
            $res6 = $st6->fetchAll();


            $st1 = null;
            $st2 = null;
            $st3 = null;
            $st4 = null;
            $st5 = null;
            $st6 = null;
            $pdo2 = null;

            $st = null;
            $pdo = null;

            $myLike = $res1[0];
            $myScrap = $res2[0];
            $myPost = $res3[0];
            $url = $res4;
            $commentsCount = $res5[0];
            $nationalName = $res6[0];

            $int = $int + 1;

            $row['myLike'] = $myLike['myLike'];
            $row['myScrap'] = $myScrap['myScrap'];
            $row['myPost'] = $myPost['myPost'];
            $row['imageUrl'] = $url;
            $row['commentsCount'] = $commentsCount['commentsCount'];
            $row['nationalName'] = $nationalName['nationalName'];

            array_push($post_result, $row);
        }
        return $post_result;
    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        throw $e;
        return 2;
    }
}

function postLike($postNo, $userNo)
{
    $no = (int)0;
    $is_deleted = (string)'N';

    $pdo = pdoSqlConnect();
    $query = "insert into likes (no, user_no, post_no, is_deleted) values (?,?,?,?);";

    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$no, $userNo, $postNo, $is_deleted]);

    $st = null;
    $pdo = null;
}

function deleteLike($userNo, $postNo)
{
    $is_deleted = (string)'Y';
    $pdo = pdoSqlConnect();
    $query = "UPDATE likes
                      SET is_deleted = ?
                     WHERE user_no = ? and post_no = ?;";

    $st = $pdo->prepare($query);
    $st->execute([$is_deleted, $userNo, $postNo]);
    $st = null;
    $pdo = null;
}

function postScrap($photoResult, $scrapName, $closure, $userNo)
{
    try {
        $int = 0;
        $no = (int)0;
        $is_deleted = (string)'N';
        $is_registered = (string)date("Y-m-d H:i:s");
        $count = count($photoResult);
        $question_marks = str_repeat(",(?,?,?,?)", $count - 1);

        $pdo = pdoSqlConnect();
        $query = "insert into images (no, is_deleted, url, is_registered, user_no, type) values (?,?,?,?,?,?)$question_marks;";
        $st = $pdo->prepare($query);
        //    $st->execute([$param,$param]);
        foreach ($photoResult as $row => $value) //이미지
        {
            $st->bindValue($int + 1, $no);
            $st->bindValue($int + 2, $is_deleted);
            $st->bindValue($int + 3, $value);
            $st->bindValue($int + 4, $is_registered);
            $st->bindValue($int + 5, $userNo);
            $st->bindValue($int + 6, 3);

            $int = $int + 6;
        }
        $st->execute();

        $st = null;
        $pdo = null;

        $pdo = pdoSqlConnect();
        $query = "insert into scraps (no, title, title_image_no, registered_timestamp, is_deleted, user_no, is_closure) values (?,?,(select no from images order by is_registered desc limit 1),?,?,?,?);";

        $st = $pdo->prepare($query);
        $st->execute([$no, $scrapName, $is_registered, $is_deleted, $userNo, $closure]);

        $st = null;
        $pdo = null;

    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
        return 2;
    }
}

function getScrap($userNo)
{
    $is_deleted = 'N';
    $pdo = pdoSqlConnect();
    $query = "SELECT scraps.no, url, title, is_closure
FROM scraps
         inner join images on scraps.title_image_no = images.no
where scraps.user_no = ?
  and scraps.is_deleted = ?;";

    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$userNo, $is_deleted]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}

function doScrap($scrapNo, $postNo, $userNo)
{
    $no = (int)0;
    $is_deleted = (string)'N';
    $is_registered = (string)date("Y-m-d H:i:s");

    $pdo = pdoSqlConnect();
    $query = "insert into scrap_relations (no, scrap_no, post_no, registered_timestamp, is_deleted, user_no)  values (?,?,?,?,?,?);";

    $st = $pdo->prepare($query);
    $st->execute([$no, $scrapNo, $postNo, $is_registered, $is_deleted, $userNo]);

    $st = null;
    $pdo = null;
}

function deletedScrap($userNo, $scrapNo)
{
    $is_deleted = 'Y';
    $pdo = pdoSqlConnect();
    $query = "update scrap_relations set is_deleted = ? where scrap_no = ?;";
    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$is_deleted, $scrapNo]);
    $st = null;
    $pdo = null;

    $is_deleted = 'Y';
    $pdo = pdoSqlConnect();
    $query = "UPDATE scraps
                 SET is_deleted = ?
                WHERE no = ? and user_no = ?;";

    $st = $pdo->prepare($query);
    $st->execute([$is_deleted, $scrapNo, $userNo]);

    $st = null;
    $pdo = null;
}

function dontScap($postNo, $scrapNo)
{
    $is_deleted = 'Y';
    $pdo = pdoSqlConnect();
    $query = "UPDATE scrap_relations
                 SET is_deleted = ?
                WHERE scrap_no = ? and post_no = ?;";

    $st = $pdo->prepare($query);
    $st->execute([$is_deleted, $scrapNo, $postNo]);

    $st = null;
    $pdo = null;
}

function dontScapByUserNo($postNo, $userNo)
{
    try {
        $is_deleted = 'Y';
        $pdo = pdoSqlConnect();
        $query = "UPDATE scrap_relations
                 SET is_deleted = ?
                WHERE user_no = ? and post_no = ?;";

        $st = $pdo->prepare($query);
        $st->execute([$is_deleted, $userNo, $postNo]);

        $st = null;
        $pdo = null;

        return 1;
    } catch (Exception $e) {
//        echo $e;
        return 2;
    }
}

function getHome($nationalArr, $userNo, $page, $size)
{
    try {

        $post_result = array();
        $userNo = (int)$userNo;
        $page = (int)$page;
        $size = (int)$size;
        $is_deleted = (string)'N';
        $int = 0;
        $pdo = pdoSqlConnect();
        $query = "select getlike.postNo, nickname, getlike.user_no as userNo,profileUrl, getlike.registered_timestamp, contents, likedCount, count(scrap_relations.no) as scrapCount from(
select getUser.postNo, nickname, getUser.user_no,profileUrl, registered_timestamp, contents, count(likes.no) as likedCount
from (select postinUSER.no as postNo, nickname, postinUSER.user_no, images.url as profileUrl, postinUSER.name, postinUSER.registered_timestamp,contents
from users inner join images inner join (select * from posts inner join locations on posts.location_no = locations.nationalNo 
where ";
        $whereString = "(";
        if ($nationalArr[0] == 0) {
            $whereString = "1 = 1 ";
        } else {
            for ($i = 0; $i < count($nationalArr); $i++) {
                $nationalNo = $nationalArr[$i];
                if ($i == count($nationalArr) - 1) {
                    $whereString = $whereString . "location_no = " . $nationalNo . ") ";

                } else {
                    $whereString = $whereString . "location_no = " . $nationalNo . " or ";
                }
            }
        }
        $query = $query . $whereString;
        $queryBack = "and is_deleted = :deleted1) postinUSER
    on users.no = postinUSER.user_no and users.profile_image_no = images.no and postinUSER.user_no = users.no) getUser
left outer join likes
    on getUser.postNo = likes.post_no and likes.is_deleted = :deleted2 group by getUser.postNo, nickname, profileUrl, registered_timestamp, contents) getlike
left outer join scrap_relations on getlike.postNo = scrap_relations.post_no and scrap_relations.is_deleted = :deleted3
group by getlike.postNo, nickname, profileUrl, registered_timestamp, contents, likedCount order by registered_timestamp DESC limit :page, :siz;";
        $query = $query . $queryBack;
        $st = $pdo->prepare($query);
//        echo $query;
        $st->bindParam(':deleted1', $is_deleted, PDO::PARAM_STR);
        $st->bindParam(':deleted2', $is_deleted, PDO::PARAM_STR);
        $st->bindParam(':deleted3', $is_deleted, PDO::PARAM_STR);
        $st->bindParam(':page', $page, PDO::PARAM_INT);
        $st->bindParam(':siz', $size, PDO::PARAM_INT);

        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

//    where location_no = 1 OR location_no = 2 OR location_no = 3

        foreach ($res as $row) {
            $postNo = $row['postNo'];
//            echo $postNo;

            $pdo2 = pdoSqlConnect();
            $querymyLike = "select exists(select * from posts inner join likes on posts.no = likes.post_no where post_no = ? and likes.is_deleted = ? and likes.user_no = ?) as myLike;";
            $st1 = $pdo2->prepare($querymyLike);
            $querymyScrap = "select exists(select * from posts inner join scrap_relations on posts.no = scrap_relations.post_no where post_no = ? and scrap_relations.is_deleted = ? and scrap_relations.user_no = ?) as myScrap;";
            $st2 = $pdo2->prepare($querymyScrap);
            $querymyPost = "select exists (select * from posts inner join users on posts.user_no = users.no where posts.no = ? and posts.user_no = ? )as myPost;";
            $st3 = $pdo2->prepare($querymyPost);
            $queryimageUrl = "select distinct url as imageUrl from posts inner join post_image_relations inner join images WHERE posts.no = post_image_relations.post_no AND post_image_relations.post_image_no = images.no AND posts.no =? and images.is_deleted = ?;";
            $st4 = $pdo2->prepare($queryimageUrl);
            $querycomments = "select count(*)as commentsCount from posts inner join comments on comments.post_no = posts.no where posts.no = ? and comments.is_deleted = ? order by comments.comment_sequence;";
            $st5 = $pdo2->prepare($querycomments);
            $queryNational = "select locations.name as nationalName from posts inner join locations on posts.location_no = locations.nationalNo where posts.no = ?;";
            $st6 = $pdo2->prepare($queryNational);

            $pdo2->beginTransaction();
            $st1->execute([$postNo, $is_deleted, $userNo]);
            $st2->execute([$postNo, $is_deleted, $userNo]);
            $st3->execute([$postNo, $userNo]);
            $st4->execute([$postNo, $is_deleted]);
            $st5->execute([$postNo, $is_deleted]);
            $st6->execute([$postNo]);
            $pdo2->commit();

            $st1->setFetchMode(PDO::FETCH_ASSOC);
            $res1 = $st1->fetchAll();
            $st2->setFetchMode(PDO::FETCH_ASSOC);
            $res2 = $st2->fetchAll();
            $st3->setFetchMode(PDO::FETCH_ASSOC);
            $res3 = $st3->fetchAll();
            $st4->setFetchMode(PDO::FETCH_ASSOC);
            $res4 = $st4->fetchAll();
            $st5->setFetchMode(PDO::FETCH_ASSOC);
            $res5 = $st5->fetchAll();
            $st6->setFetchMode(PDO::FETCH_ASSOC);
            $res6 = $st6->fetchAll();


            $st1 = null;
            $st2 = null;
            $st3 = null;
            $st4 = null;
            $st5 = null;
            $st6 = null;
            $pdo2 = null;

            $st = null;
            $pdo = null;

            $myLike = $res1[0];
            $myScrap = $res2[0];
            $myPost = $res3[0];
            $url = $res4;
            $commentsCount = $res5[0];
            $nationalName = $res6[0];

            $int = $int + 1;

            $row['myLike'] = $myLike['myLike'];
            $row['myScrap'] = $myScrap['myScrap'];
            $row['myPost'] = $myPost['myPost'];
            $row['imageUrl'] = $url;
//            echo json_encode($url);
            $row['commentsCount'] = $commentsCount['commentsCount'];
            $row['nationalName'] = $nationalName['nationalName'];

            array_push($post_result, $row);

        }

        return $post_result;
    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
    }
}

function getCommnets($postNo) //수정필요
{
    try {

        $comments_result = array();
        $sequence = (int)1;
        $is_deleted = (string)'N';
        $pdo = pdoSqlConnect();
        $query = "select comments.no as commentsNo, userNo, nickname, url as profileImage, comments.registered_timestamp as registerAt, contents
from (select comments.no, users.no as userNo, nickname, profile_image_no, comments.registered_timestamp, contents
      from comments
               inner join users on comments.user_no = users.no
      where comment_sequence = ?
        and post_no = ?
        and comments.is_deleted = ?
      order by no) comments
         inner join images on comments.profile_image_no = images.no order by registerAt desc;";

        $st = $pdo->prepare($query);
        //    $st->execute([$param,$param]);
        $st->execute([$sequence, $postNo, $is_deleted]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        $st = null;
        $pdo = null;

        foreach ($res as $row) {
            $commentsNo = $row['commentsNo'];

//            echo $commentsNo;

            $pdo = pdoSqlConnect();
            $is_deleted = (string)'N';
            $query = "select count(*) as commentsCount
from comments
         inner join users on comments.user_no = users.no
where comments.is_deleted = ?
and (select comment_sequence from comments where no = ?) < comment_sequence
and tag_no = ?
order by comments.comment_sequence;";

            $st = $pdo->prepare($query);
            //    $st->execute([$param,$param]);
            $st->execute([$is_deleted, $commentsNo, $commentsNo]);
            $st->setFetchMode(PDO::FETCH_ASSOC);
            $res = $st->fetchAll();

            $st = null;
            $pdo = null;

            $row['commentsCount'] = $res[0]['commentsCount'];
//
            array_push($comments_result, $row);
        }
        return $comments_result;
    } catch (Exception $e) {
        echo $e;
    }
}

function postComments_without_tag($userNo, $postNo, $content)
{
    try {
        $no = (int)0;
        $is_deleted = (string)'N';
        $timestamp = date("Y-m-d H:i:s");
        $sequence = (int)1;
        $pdo = pdoSqlConnect();
        $query = "INSERT INTO comments (no, user_no, post_no, registered_timestamp, comment_sequence,
                      contents, is_deleted)
VALUES (?, ?, ?, ?, ?, ?, ?);";

        $st = $pdo->prepare($query);
        $st->bindParam(1, $no, PDO::PARAM_INT);
        $st->bindParam(2, $userNo, PDO::PARAM_INT);
        $st->bindParam(3, $postNo, PDO::PARAM_INT);
        $st->bindParam(4, $timestamp, PDO::PARAM_STR);
        $st->bindParam(5, $sequence, PDO::PARAM_INT);
        $st->bindParam(6, $content, PDO::PARAM_STR);
        $st->bindParam(7, $is_deleted, PDO::PARAM_STR);
        $st->execute();

        $st = null;
        $pdo = null;
    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
        return 2;
    }
}

function postComments($userNo, $postNo, $content, $tagNo)
{
    try {

        $no = (int)0;
        $is_deleted = (string)'N';
        $timestamp = date("Y-m-d H:i:s");

        $pdo = pdoSqlConnect();
        $query = "select comment_sequence from comments where no = ?;";

        $st = $pdo->prepare($query);
        //    $st->execute([$param,$param]);
        $st->execute([$tagNo]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        $st = null;
        $pdo = null;

        $sequence = $res[0]['comment_sequence']; //taguser_seqeunce
        $sequence = $sequence + 1;

        $pdo = pdoSqlConnect();
        $query = "
INSERT INTO comments (no, user_no, post_no, tag_no, registered_timestamp, comment_sequence,
                      contents, is_deleted)
VALUES (?, ?, ?, ?, ?, ? ,?, ?);";

        $st = $pdo->prepare($query);
        $st->bindParam(1, $no, PDO::PARAM_INT);
        $st->bindParam(2, $userNo, PDO::PARAM_INT);
        $st->bindParam(3, $postNo, PDO::PARAM_INT);
        $st->bindParam(4, $tagNo, PDO::PARAM_INT);
        $st->bindParam(5, $timestamp, PDO::PARAM_INT);
        $st->bindParam(6, $sequence, PDO::PARAM_INT);
        $st->bindParam(7, $content, PDO::PARAM_STR);
        $st->bindParam(8, $is_deleted, PDO::PARAM_STR);
        $st->execute();

        $st = null;
        $pdo = null;
    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        throw $e;
        return 2;
    }
}

function getTagcomments($commentsNo, $tagNo)
{
    $is_deleted = (string)'N';
    $pdo = pdoSqlConnect();
    $query = "select commnetsNo                    as commnetsNo,
       userNo,
       nickname,
       url                           as profileImage,
       comments.registered_timestamp as registerAt,
       comments.contents
from (select comments.no      as commnetsNo,
             comments.user_no as userNo,
             nickname,
             comments.registered_timestamp,
             users.profile_image_no,
             comments.contents
      from comments
               inner join users on comments.user_no = users.no
      where comments.no >= ?
        and comments.is_deleted = ?
        and comments.comment_sequence > 1
        and tag_no = ?
      order by comments.comment_sequence desc) comments
         inner join images on comments.profile_image_no = images.no;";

    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$commentsNo, $is_deleted, $tagNo]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}


function getscrapHome($scrapNo, $userNo, $page)
{

    try {
        $int = (int)0;
        $post_result = array();
        $is_deleted = (string)'N';
        $size = (int)20;
        $pdo = pdoSqlConnect();
        $query = "

select getlike.postNo,
       nickname,
       getlike.user_no           as userNo,
       profileUrl,
       getlike.registered_timestamp,
       contents,
       likedCount,
       count(scrap_relations.no) as scrapCount
from (
         select getUser.postNo,
                nickname,
                getUser.user_no,
                profileUrl,
                registered_timestamp,
                contents,
                scrapTime,
                count(likes.no) as likedCount
         from (select postinUSER.no as postNo,
                      nickname,
                      postinUSER.user_no,
                      images.url    as profileUrl,
                      postinUSER.name,
                      postinUSER.registered_timestamp,
                      scrapTime,
                      contents
               from users
                        inner join images
                        inner join (select *
                                    from (select *
                                          from posts
                                                   inner join (select post_no, scrap_relations.registered_timestamp as scrapTime
                                                               from scrap_relations
                                                               where is_deleted = :isDeleted
                                                                 and scrap_no = :scrapNo) onePost
                                                              on posts.no = onePost.post_no where posts.is_deleted = :postdeleted) onePost1
                                             inner join locations on onePost1.location_no = locations.nationalNo
               ) postinUSER
                                   on users.no = postinUSER.user_no and users.profile_image_no = images.no and
                                      postinUSER.user_no = users.no) getUser
                  left outer join likes
                                  on getUser.postNo = likes.post_no and likes.is_deleted = :deleted2
         group by getUser.postNo, nickname, profileUrl, registered_timestamp, contents, scrapTime) getlike
         left outer join scrap_relations on getlike.postNo = scrap_relations.post_no and scrap_relations.is_deleted = :deleted3
group by getlike.postNo, nickname, profileUrl, registered_timestamp, contents, likedCount,scrapTime
order by scrapTime DESC
limit :page,
    :siz;";

        $st = $pdo->prepare($query);
//        $st->bindParam(1, $is_deleted, PDO::PARAM_STR);
        $st->bindParam(':isDeleted', $is_deleted, PDO::PARAM_STR);
        $st->bindParam(':scrapNo', $scrapNo, PDO::PARAM_INT);
        $st->bindParam(':postdeleted', $is_deleted, PDO::PARAM_STR);
        $st->bindParam(':deleted2', $is_deleted, PDO::PARAM_STR);
        $st->bindParam(':deleted3', $is_deleted, PDO::PARAM_STR);
        $st->bindParam(':page', $page, PDO::PARAM_INT);
        $st->bindParam(':siz', $size, PDO::PARAM_INT);

        $st->execute();


        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        $st = null;
        $pdo = null;

//        return $res;

        foreach ($res as $row) {
            $postNo = $row['postNo'];

            $pdo2 = pdoSqlConnect();
            $querymyLike = "select exists(select * from posts inner join likes on posts.no = likes.post_no where post_no = ? and likes.is_deleted = ? and likes.user_no = ?) as myLike;";
            $st1 = $pdo2->prepare($querymyLike);
            $querymyScrap = "select exists(select * from posts inner join scrap_relations on posts.no = scrap_relations.post_no where post_no = ? and scrap_relations.is_deleted = ? and scrap_relations.user_no = ?) as myScrap;";
            $st2 = $pdo2->prepare($querymyScrap);
            $querymyPost = "select exists (select * from posts inner join users on posts.user_no = users.no where posts.no = ? and posts.user_no = ? )as myPost;";
            $st3 = $pdo2->prepare($querymyPost);
            $queryimageUrl = "select distinct url as imageUrl from posts inner join post_image_relations inner join images WHERE posts.no = post_image_relations.post_no AND post_image_relations.post_image_no = images.no AND posts.no =? and images.is_deleted = ?;";
            $st4 = $pdo2->prepare($queryimageUrl);
            $querycomments = "select count(*)as commentsCount from posts inner join comments on comments.post_no = posts.no where posts.no = ? and comments.is_deleted = ? order by comments.comment_sequence;";
            $st5 = $pdo2->prepare($querycomments);
            $queryNational = "select locations.name as nationalName from posts inner join locations on posts.location_no = locations.nationalNo where posts.no = ?;";
            $st6 = $pdo2->prepare($queryNational);

            $pdo2->beginTransaction();
            $st1->execute([$postNo, $is_deleted, $userNo]);
            $st2->execute([$postNo, $is_deleted, $userNo]);
            $st3->execute([$postNo, $userNo]);
            $st4->execute([$postNo, $is_deleted]);
            $st5->execute([$postNo, $is_deleted]);
            $st6->execute([$postNo]);
            $pdo2->commit();

            $st1->setFetchMode(PDO::FETCH_ASSOC);
            $res1 = $st1->fetchAll();
            $st2->setFetchMode(PDO::FETCH_ASSOC);
            $res2 = $st2->fetchAll();
            $st3->setFetchMode(PDO::FETCH_ASSOC);
            $res3 = $st3->fetchAll();
            $st4->setFetchMode(PDO::FETCH_ASSOC);
            $res4 = $st4->fetchAll();
            $st5->setFetchMode(PDO::FETCH_ASSOC);
            $res5 = $st5->fetchAll();
            $st6->setFetchMode(PDO::FETCH_ASSOC);
            $res6 = $st6->fetchAll();


            $st1 = null;
            $st2 = null;
            $st3 = null;
            $st4 = null;
            $st5 = null;
            $st6 = null;
            $pdo2 = null;

            $st = null;
            $pdo = null;

            $myLike = $res1[0];
            $myScrap = $res2[0];
            $myPost = $res3[0];
            $url = $res4;
            $commentsCount = $res5[0];
            $nationalName = $res6[0];
            $int = $int + 1;

            $row['myLike'] = $myLike['myLike'];
            $row['myScrap'] = $myScrap['myScrap'];
            $row['myPost'] = $myPost['myPost'];
            $row['imageUrl'] = $url;
            $row['commentsCount'] = $commentsCount['commentsCount'];
            $row['nationalName'] = $nationalName['nationalName'];

            array_push($post_result, $row);

        }
        return $post_result;

    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
    }
}

function patchScrap($userNo, $scrapNo, $closure, $scrapName, $photoResult)
{
    try {
        $no = (int)0;
        $is_deleted = 'N';
        $is_registered = (string)date("Y-m-d H:i:s");

        $pdo = pdoSqlConnect();
        $query = "select im.no
                from images as im
                         inner join scraps as s on im.no = s.title_image_no
                where s.no = ?;";

        $st = $pdo->prepare($query);
        //    $st->execute([$param,$param]);
        $st->execute([$scrapNo]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        $st = null;
        $pdo = null;

        $imageNo = $res[0]['no'];
        $is_deleted2 = 'Y';

        $pdo = pdoSqlConnect();
        $query = "UPDATE images SET is_deleted= ? WHERE no = ?   and is_deleted = ?;";

        $st = $pdo->prepare($query);
        $st->execute([$is_deleted2, $imageNo, $is_deleted]);
        $st = null;
        $pdo = null;

        $pdo = pdoSqlConnect();
        $query = "INSERT INTO images (no, is_deleted, url, is_registered, user_no, type) VALUES (?,?,?,?,?,?);";

        $st = $pdo->prepare($query);
        foreach ($photoResult as $row => $value) //이미지
        {
            $st->bindValue(1, $no);
            $st->bindValue(2, $is_deleted);
            $st->bindValue(3, $value);
            $st->bindValue(4, $is_registered);
            $st->bindValue(5, $userNo);
            $st->bindValue(6, 2);
        }
        $st->execute();

        $st = null;
        $pdo = null;

        $pdo = pdoSqlConnect();
        $query = "SELECT no FROM images where is_deleted = ? order by no desc limit 1;";

        $st = $pdo->prepare($query);
        //    $st->execute([$param,$param]);
        $st->execute([$is_deleted]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        $st = null;
        $pdo = null;

        $imageNo_reuslt = $res[0]['no'];

        $pdo = pdoSqlConnect();
        $query = "UPDATE scraps
                        SET title_image_no = ?,
                            title = ?,
                            is_closure = ?
                        WHERE NO = ? and is_deleted = ? and user_no = ?;";

        $st = $pdo->prepare($query);
        $st->execute([$imageNo_reuslt, $scrapName, $closure, $scrapNo, $is_deleted, $userNo]);
        $st = null;
        $pdo = null;
    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
        return 2;
    }

}

function deletePost($postNo, $userNo, $imageNoList)
{
    try {

        $is_deleted = 'Y';
        $pdo = pdoSqlConnect();
        $query = "UPDATE posts
                        SET is_deleted = ?
                        WHERE NO = ? and user_no = ?;";

        $st1 = $pdo->prepare($query);

//        echo json_encode($imageNoList, JSON_NUMERIC_CHECK);
        $pdo->beginTransaction();
        $st1->execute([$is_deleted, $postNo, $userNo]);


        foreach ($imageNoList as $item) {
            $query = "UPDATE images SET is_deleted = 'Y' WHERE no = ?;";
            $st2 = $pdo->prepare($query);
            $st2->execute([$item['imageNo']]);
//            echo $query.$item['imageNo'];
        }

        $pdo->commit();


        $st = null;
        $pdo = null;
    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
        return 2;
    }
}

function patchPost($postNo, $userNo, $photoResult, $nationalNo, $text)
{
    try {
        $nowTime = (string)date("Y-m-d H:i:s");
        $is_deleted = 'N';

        $pdo = pdoSqlConnect();
        $pdo->beginTransaction();


        $query = "UPDATE posts
                        SET contents = ?,
                            location_no = ?,
                            updated_timestamp = ?
                        WHERE NO = ? and is_deleted = ? and user_no = ?;";

        $st1 = $pdo->prepare($query);
        $st1->execute([$text, $nationalNo, $nowTime, $postNo, $is_deleted, $userNo]);


        $imageNoList = getPostImageNo($postNo);
        foreach ($imageNoList as $item) {
            $query = "UPDATE images SET is_deleted = 'Y' WHERE no = ?;";
            $st2 = $pdo->prepare($query);
            $st2->execute([$item['imageNo']]);
        }

        foreach ($photoResult as $row => $value) //이미지
        {
            $query = "insert into images (is_deleted, url, user_no, type, is_registered) values ('N',:url,:userNo,1, :now);";
            $st = $pdo->prepare($query);
            $st->bindValue(':url', $value, PDO::PARAM_INT);
            $st->bindValue(':userNo', $userNo, PDO::PARAM_INT);
            $st->bindValue(':now', $nowTime, PDO::PARAM_STR);

            $st->execute();
        }

        $query = "SELECT no as image_no FROM images WHERE user_no = :userNo ORDER BY no DESC LIMIT :limit;";
        $st = $pdo->prepare($query);
        $st->bindValue(':userNo', $userNo, PDO::PARAM_INT);
        $st->bindValue(':limit', count($photoResult), PDO::PARAM_INT);
        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        foreach ($res as $item) {
            $query = "insert into post_image_relations (post_image_no, post_no) values (:postImageNo, :postNo);";
            $st = $pdo->prepare($query);
            $result_image_no = $item['image_no'];
            $st->bindValue(':postImageNo', $result_image_no, PDO::PARAM_INT);
            $st->bindValue(':postNo', $postNo, PDO::PARAM_INT);
            $st->execute();
        }

        $pdo->commit();

    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            echo $e;
            $pdo->rollback();
        }
        return 2;
    }

}

function postCheck($postNo)
{

    $is_deleted = 'N';
    $pdo = pdoSqlConnect();
    $query = "select exists(select * from posts where no = ? and is_deleted = ?)as exist;";
//        echo $query;
    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$postNo, $is_deleted]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res2 = $st->fetchAll();

    $st = null;
    $pdo = null;

    $secondResult = $res2[0]['exist'];


    if ($secondResult == 1) {
        return 1;
    } else
        return 0;

}

function scrapCheck($userNo, $scrapNo)
{
    $is_deleted = 'N';
    $pdo = pdoSqlConnect();
    $query = "select exists(select * from scraps where user_no = ? and no = ? and is_deleted = ?)as exist;";
//        echo $query;
    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$userNo, $scrapNo, $is_deleted]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res[0]['exist'];
}

function likeCheck($userNo, $postNo)
{
    $is_deleted = 'N';

    $pdo = pdoSqlConnect();
    $query = "select exists(select * from likes where user_no = ? and post_no = ? and is_deleted = ?)as exist;";
//        echo $query;
    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$userNo, $postNo, $is_deleted]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res[0]['exist'];
}


function convert_to_userNo($email)
{
    $pdo = pdoSqlConnect();
    $query = "select no from users where email = ?;";
//        echo $query;
    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$email]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res[0]['no'];
}

function isValidJWToken($email, $password)
{

    $pdo = pdoSqlConnect();
    $query = "SELECT EXISTS(SELECT * FROM users WHERE email = ? and password = ?) AS exist";
//        echo $query;
    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$email, $password]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return array("intval" => intval($res[0]["exist"]), "email" => $email);
}

function isScrapedPost($userNo, $postNo)
{

//    echo $userNo, $postNo;
    $is_deleted = 'N';
    $pdo = pdoSqlConnect();
    $query = "select exists(select * from scrap_relations where user_no = ? and post_no = ? and is_deleted = ?)as exist;";
//        echo $query;
    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$userNo, $postNo, $is_deleted]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res[0]['exist'];
}

function getPost_word($userNo, $nationalArr, $word, $page)
{
    try {

        $post_result = array();

        $a = '%';
        $word = (string)$a . $word . $a;
        $userNo = (int)$userNo;
        $page = (int)$page;
        $size = (int)20;
        $is_deleted = (string)'N';
        $int = 0;
        $pdo = pdoSqlConnect();
        $query = "select getlike.postNo, nickname, getlike.user_no as userNo,profileUrl, getlike.registered_timestamp, contents, likedCount, count(scrap_relations.no) as scrapCount from(
select getUser.postNo, nickname, getUser.user_no,profileUrl, registered_timestamp, contents, count(likes.no) as likedCount
from (select postinUSER.no as postNo, nickname, postinUSER.user_no, images.url as profileUrl, postinUSER.name, postinUSER.registered_timestamp,contents
from users inner join images inner join (select * from posts inner join locations on posts.location_no = locations.nationalNo 
where ";
        $whereString = "(";
        if ($nationalArr[0] == 0) {
            $whereString = "1 = 1 ";
        } else {
            for ($i = 0; $i < count($nationalArr); $i++) {
                $nationalNo = $nationalArr[$i];
                if ($i == count($nationalArr) - 1) {
                    $whereString = $whereString . "location_no = " . $nationalNo . ") ";

                } else {
                    $whereString = $whereString . "location_no = " . $nationalNo . " or ";
                }
            }
        }
        $query = $query . $whereString;
        $queryBack = "and is_deleted = :deleted1) postinUSER
    on users.no = postinUSER.user_no and users.profile_image_no = images.no and postinUSER.user_no = users.no  where (contents like :content)
                  or (nickname like :nickname)) getUser
left outer join likes
    on getUser.postNo = likes.post_no and likes.is_deleted = :deleted2 group by getUser.postNo, nickname, profileUrl, registered_timestamp, contents) getlike
left outer join scrap_relations on getlike.postNo = scrap_relations.post_no and scrap_relations.is_deleted = :deleted3
group by getlike.postNo, nickname, profileUrl, registered_timestamp, contents, likedCount order by registered_timestamp DESC limit :page, :siz;";
        $query = $query . $queryBack;
        $st = $pdo->prepare($query);
//        echo $query;
        $st->bindParam(':deleted1', $is_deleted, PDO::PARAM_STR);
        $st->bindParam(':content', $word, PDO::PARAM_STR);
        $st->bindParam(':nickname', $word, PDO::PARAM_STR);
        $st->bindParam(':deleted2', $is_deleted, PDO::PARAM_STR);
        $st->bindParam(':deleted3', $is_deleted, PDO::PARAM_STR);
        $st->bindParam(':page', $page, PDO::PARAM_INT);
        $st->bindParam(':siz', $size, PDO::PARAM_INT);

        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

//    where location_no = 1 OR location_no = 2 OR location_no = 3

        foreach ($res as $row) {
            $postNo = $row['postNo'];
//            echo $postNo;

            $pdo2 = pdoSqlConnect();
            $querymyLike = "select exists(select * from posts inner join likes on posts.no = likes.post_no where post_no = ? and likes.is_deleted = ? and likes.user_no = ?) as myLike;";
            $st1 = $pdo2->prepare($querymyLike);
            $querymyScrap = "select exists(select * from posts inner join scrap_relations on posts.no = scrap_relations.post_no where post_no = ? and scrap_relations.is_deleted = ? and scrap_relations.user_no = ?) as myScrap;";
            $st2 = $pdo2->prepare($querymyScrap);
            $querymyPost = "select exists (select * from posts inner join users on posts.user_no = users.no where posts.no = ? and posts.user_no = ? )as myPost;";
            $st3 = $pdo2->prepare($querymyPost);
            $queryimageUrl = "select distinct url as imageUrl from posts inner join post_image_relations inner join images WHERE posts.no = post_image_relations.post_no AND post_image_relations.post_image_no = images.no AND posts.no =? and images.is_deleted = ?;";
            $st4 = $pdo2->prepare($queryimageUrl);
            $querycomments = "select count(*)as commentsCount from posts inner join comments on comments.post_no = posts.no where posts.no = ? and comments.is_deleted = ? order by comments.comment_sequence;";
            $st5 = $pdo2->prepare($querycomments);
            $queryNational = "select locations.name as nationalName from posts inner join locations on posts.location_no = locations.nationalNo where posts.no = ?;";
            $st6 = $pdo2->prepare($queryNational);

            $pdo2->beginTransaction();
            $st1->execute([$postNo, $is_deleted, $userNo]);
            $st2->execute([$postNo, $is_deleted, $userNo]);
            $st3->execute([$postNo, $userNo]);
            $st4->execute([$postNo, $is_deleted]);
            $st5->execute([$postNo, $is_deleted]);
            $st6->execute([$postNo]);
            $pdo2->commit();

            $st1->setFetchMode(PDO::FETCH_ASSOC);
            $res1 = $st1->fetchAll();
            $st2->setFetchMode(PDO::FETCH_ASSOC);
            $res2 = $st2->fetchAll();
            $st3->setFetchMode(PDO::FETCH_ASSOC);
            $res3 = $st3->fetchAll();
            $st4->setFetchMode(PDO::FETCH_ASSOC);
            $res4 = $st4->fetchAll();
            $st5->setFetchMode(PDO::FETCH_ASSOC);
            $res5 = $st5->fetchAll();
            $st6->setFetchMode(PDO::FETCH_ASSOC);
            $res6 = $st6->fetchAll();


            $st1 = null;
            $st2 = null;
            $st3 = null;
            $st4 = null;
            $st5 = null;
            $st6 = null;
            $pdo2 = null;

            $st = null;
            $pdo = null;

            $myLike = $res1[0];
            $myScrap = $res2[0];
            $myPost = $res3[0];
            $url = $res4;
            $commentsCount = $res5[0];
            $nationalName = $res6[0];

            $int = $int + 1;

            $row['myLike'] = $myLike['myLike'];
            $row['myScrap'] = $myScrap['myScrap'];
            $row['myPost'] = $myPost['myPost'];
            $row['imageUrl'] = $url;
//            echo json_encode($url);
            $row['commentsCount'] = $commentsCount['commentsCount'];
            $row['nationalName'] = $nationalName['nationalName'];

            array_push($post_result, $row);

        }

        return $post_result;
    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
    }
}

function getnick($word, $page, $userNo)
{
    $a = '%';
    $word = (string)$a . $word . $a;
    $is_deleted = (int)0;
    $page = (int)$page;
    $pdo = pdoSqlConnect();
    $query = "select users.no as userNo, url as profileImage, nickname as nickname
from users
         inner join images on users.profile_image_no = images.no
where users.nickname like ?
  and users.is_deleted = ?
  and users.no not in (?)
limit ?, 20;
";
//        echo $query;
    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->bindParam(1, $word, PDO::PARAM_STR);
    $st->bindParam(2, $is_deleted, PDO::PARAM_INT);
    $st->bindParam(3, $userNo, PDO::PARAM_INT);
    $st->bindParam(4, $page, PDO::PARAM_INT);
    $st->execute();
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}

function postScrapCheck($userNo, $postNo)
{
    $is_deleted = 'N';
    $pdo = pdoSqlConnect();
    $query = "select exists(select * from scrap_relations where user_no = ? and post_no = ? and is_deleted = ?)as exist;";
//        echo $query;
    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$userNo, $postNo, $is_deleted]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res[0]['exist'];
}