<?php

function getHaveRouteInfectedList()
{
    $pdo = pdoSqlConnect();
    $query = "SELECT infectedNo FROM infected WHERE haveRoute = 1 ;";

    $st = $pdo->prepare($query);
    $st->execute();
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;
//    echo json_encode($res, JSON_NUMERIC_CHECK);

    return $res;

}


function getRoute($infectedNo)
{
    $pdo = pdoSqlConnect();
    $query = "SELECT infected.infectedNo, infectedRoute.name as routeName, latitude,longitude,content, infectedRoute.date,
       infected.name, isolationDate, hospital, contactCount, age, isNewRoute
FROM infectedRoute,
     infected
WHERE infected.infectedNo = ? AND
      infected.infectedNo = infectedRoute.infectedNo ORDER BY date;";

    $st = $pdo->prepare($query);
    //    $st->execute([$param,$param]);
    $st->execute([$infectedNo]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}

function getClinic()
{
    $pdo = pdoSqlConnect();
    $query = "SELECT * FROM clinic WHERE latitude is not null;;";

    $st = $pdo->prepare($query);
    $st->execute([]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}

function getHospital()
{
    $pdo = pdoSqlConnect();
    $query = "SELECT * FROM isolationHospital;";

    $st = $pdo->prepare($query);
    $st->execute([]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}

function getLastStatistic()
{
    $pdo = pdoSqlConnect();
    $query = "SELECT * FROM statistics ORDER BY timestamp DESC limit 1;";

    $st = $pdo->prepare($query);
    $st->execute([]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res[0];
}

function getStatistic()
{
    $last = getLastStatistic();
    $pdo = pdoSqlConnect();
    $query = "SELECT * FROM statistics ORDER BY timestamp DESC limit 2;";
    $st = $pdo->prepare($query);
    $st->execute([]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $res = $res[1];
    $st = null;
    $pdo = null;

    $obj = (Object)Array();
    $obj->infected = $last['infected'];
    $obj->dead = $last['dead'];
    $obj->cured = $last['cured'];
    $obj->symptom = $last['symptom'];
    $obj->inspection = $last['inspection'];
    $obj->isolation = $last['isolation'];

    $obj->infectedDiff = $last['infected'] - $res['infected'];
    $obj->deadDiff = $last['dead'] - $res['dead'];
    $obj->curedDiff = $last['cured'] - $res['cured'];
    $obj->symptomDiff = $last['symptom'] - $res['symptom'];
    $obj->inspectionDiff = $last['inspection'] - $res['inspection'];
    $obj->isolationDiff = $last['isolation'] - $res['isolation'];

//    if ($last['infected'] - $res['infected'] > 0) {
//        $obj->infectedDiff = "+".$last['infected'] - $res['infected'];
//    } else {
//        $obj->infectedDiff = $last['infected'] - $res['infected'];
//    }
//    if ($last['dead'] - $res['dead'] > 0) {
//        $obj->infectedDiff = "+".$last['dead'] - $res['dead'];
//    } else {
//        $obj->infectedDiff = $last['dead'] - $res['dead'];
//    }
//    if ($last['cured'] - $res['cured'] > 0) {
//        $obj->infectedDiff = "+".$last['cured'] - $res['cured'];
//    } else {
//        $obj->infectedDiff = $last['cured'] - $res['cured'];
//    }
//    if ($last['symptom'] - $res['symptom'] > 0) {
//        $obj->infectedDiff = "+".$last['symptom'] - $res['symptom'];
//    } else {
//        $obj->infectedDiff = $last['symptom'] - $res['symptom'];
//    }
//    if ($last['inspection'] - $res['inspection'] > 0) {
//        $obj->infectedDiff = "+".$last['inspection'] - $res['inspection'];
//    } else {
//        $obj->infectedDiff = $last['inspection'] - $res['inspection'];
//    }
//    if ($last['isolation'] - $res['isolation'] > 0) {
//        $obj->infectedDiff = "+".$last['isolation'] - $res['isolation'];
//    } else {
//        $obj->infectedDiff = $last['isolation'] - $res['isolation'];
//    }
    $obj->updatedTime = $last['timestamp'];

    return $obj;
}

//업데이트 체크
function checkCoronaDoctorVersionCode($versionCode)
{
    try {

        $pdo = pdoSqlConnect();
        $query = "SELECT versionCode FROM coronaDoctorVersion ORDER BY versionCode DESC";
        $st = $pdo->prepare($query);
        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        $lastVersionCode = $res[0]['versionCode'];
//        echo $lastVersionCode;

        $query = "SELECT versionCode FROM coronaDoctorVersion WHERE must_update = 1 ORDER BY versionCode DESC";
        $st = $pdo->prepare($query);
        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();

        $MustUpdateVersionCode = $res[0]['versionCode'];
//        echo $MustUpdateVersionCode;

        if ($versionCode < $MustUpdateVersionCode) {
            return 1; //업데이트 필수
        } else if ($versionCode < $lastVersionCode) {
            return 2; //업데이트 필수는아니지만 새로운 버전 있음
        } else {
            return 3;//촤신버전임
        }

    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function updateCoronaFcmToken($token)
{
    try {
        if (checkDuplicatedCoronaFcmToken($token) == 2) { // 이미 잇는 토큰
//            echo 111;
            return true;
        }
        $pdo = pdoSqlConnect();
        $query = "INSERT INTO coronaUser (fcmToken) VALUES (?);";
        $st = $pdo->prepare($query);
        $st->execute([$token]);

        $st = null;
        $pdo = null;

        return true;
    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

//이미 같은토큰이 있는지 검사
function checkDuplicatedCoronaFcmToken($token)
{
    $pdo = pdoSqlConnect();
    $query = "SELECT COUNT(*) as cnt FROM coronaUser WHERE coronaUser.fcmToken = ?;";
    $st = $pdo->prepare($query);
    $st->execute([$token]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

//    echo "cnt = ".$res[0]['cnt'];
    if ($res[0]['cnt'] == 0) {
//        echo "없음";
        return 1;
    } else {
//        echo "있음";
        return 2;
    }
}

//유저의 토큰목록 얻기
function getFcmTokenArr($pushNo)
{
    $pdo = pdoSqlConnect();
//    $query = "SELECT fcmToken FROM coronaUser;";
    $query = "SELECT * FROM coronaUser where pushNo < ? AND error is null;";
    $st = $pdo->prepare($query);
    $st->execute([$pushNo]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}

//유저의 토큰목록 얻기
function getFcmTokenArrDESC($pushNo)
{
    $pdo = pdoSqlConnect();
//    $query = "SELECT fcmToken FROM coronaUser;";
    $query = "SELECT * FROM coronaUser where pushNo < ? AND error is null ORDER BY userNo DESC;";
    $st = $pdo->prepare($query);
    $st->execute([$pushNo]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}


function sendPushMessage($fcmTokenArr, $pushNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT * FROM coronaPushHistory WHERE pushNo = ?;";
        $st = $pdo->prepare($query);
        $st->execute([$pushNo]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $count = 0;

        if ($res[0]['check']) {//진짜 푸시보내기
            if ($res[0]['postNo'] > 0) {
                $data = array("title" => $res[0]['title'],
                    "message" => $res[0]['message'],
                    "postNo" => $res[0]['postNo'],
                    "type" => $res[0]['type']);
            } else {
                $data = array("title" => $res[0]['title'],
                    "message" => $res[0]['message'],
                    "type" => $res[0]['type']);
            }
//            echo json_encode($fcmTokenArr, JSON_NUMERIC_CHECK);
            foreach ($fcmTokenArr as $row) {
                $fcmToken = $row['fcmToken'];
                if (isCanPush($row['lastPushAt']) == 1) { // 5분안에 푸시를 보냇는지 확인하여 푸시전송 (두번은못보내도록)
                    $sendFcmResult = sendFcm($fcmToken, $data, "AAAAw4hsfUc:APA91bHlR3QuaJDjycWbFOqRNSLzkK-HTj7EBzHJRoi8PlGWGK8oY0815ER2q74HFgf3zn39JP7ZP24SGgDyXhW0uM2wUhfso4a_hA66EXQzx7OnZBHBd3wpeegux6cnvrvTiEAwJ3B9", "ANDROID");
//                    $sendFcmResult = 1;
                    if ($sendFcmResult['success']) {
                        updateLastPushAt($fcmToken, $pushNo);
                        $count++;
                    } else {
                        updatePushError($fcmToken, $sendFcmResult['results'][0]['error']);
                    }
                } else {

                }
            }
            return $count;
        } else {//확인필요
            echo "푸시확인필요";
        }
    } catch (Exception $e) {
        echo $e;
        return 0;
    }


}

function getInfected()
{
    $pdo = pdoSqlConnect();
    $query = "SELECT * FROM infected WHERE haveRoute = 1 ORDER BY infectedNo DESC;";

    $st = $pdo->prepare($query);
    $st->execute();
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;
//    echo json_encode($res, JSON_NUMERIC_CHECK);

    return $res;

}


function sendPushTest($fcmToken, $title, $message, $type, $postNo)
{
    try {
        $data = array("title" => $title,
            "message" => $message,
            "type" => $type,
            "postNo" => $postNo);

        $sendFcmResult = sendFcm($fcmToken, $data, "AAAAw4hsfUc:APA91bHlR3QuaJDjycWbFOqRNSLzkK-HTj7EBzHJRoi8PlGWGK8oY0815ER2q74HFgf3zn39JP7ZP24SGgDyXhW0uM2wUhfso4a_hA66EXQzx7OnZBHBd3wpeegux6cnvrvTiEAwJ3B9", "ANDROID");

        return $sendFcmResult;

    } catch (Exception $e) {
        echo $e;
        return 0;
    }


}

function isCanPush($lastPushAt)
{


//    $lastPushAt = $res[0]['lastPushAt'];
    // 현재 시각에서 $timeA 시각을 빼기위해 strtotime으로 변환 후 뺌 (초단위)
    $timeDiff = (strtotime(date('Y-m-d H:i:s')) - strtotime($lastPushAt));
    // 결과 값은 소숫점으로 출력되는 경우가 있으므로 정수형(int)으로 캐스팅(형변환)
    $timeDiff = (int)$timeDiff;
    if ($timeDiff > 300) { // 5분이내에 두번 못보냄
//        echo $timeDiff."   ";
        return 1;
    } else {
//        echo $timeDiff."   ";
        return 999;
    }
}

function updateLastPushAt($userNo, $pushNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "UPDATE coronaUser SET lastPushAt = NOW(), pushNo = ? WHERE userNo = ?;";

        $st = $pdo->prepare($query);

        $pdo->beginTransaction();
        $st->execute([$pushNo, $userNo]);
        $pdo->commit();

        $st = null;
        $pdo = null;

    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
    }
}

function updatePushError($fcmToken, $error)
{
    $pdo = pdoSqlConnect();
    $query = "UPDATE coronaUser SET error = ? WHERE fcmToken = ?;";
    $st = $pdo->prepare($query);
    $st->execute([$error, $fcmToken]);
//    $st->setFetchMode(PDO::FETCH_ASSOC);
//    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

}


//이미 같은토큰이 있는지 검사
function getUserNoByToken($fcmToken)
{
    $pdo = pdoSqlConnect();
    $query = "SELECT userNo FROM coronaUser WHERE coronaUser.fcmToken = ?;";
    $st = $pdo->prepare($query);
    $st->execute([$fcmToken]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    if ($res[0]['userNo'] > 0) {
        return $res[0]['userNo'];
    } else {
        return -1;
    }
}

function insertPost($userNo, $title, $content)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "INSERT INTO coronaPost (userNo, title, content) VALUES (?,?,?);";
        $st = $pdo->prepare($query);
        $st->bindParam(1, $userNo, PDO::PARAM_INT);
        $st->bindParam(2, $title, PDO::PARAM_STR);
        $st->bindParam(3, $content, PDO::PARAM_STR);
        $st->execute();

        $st = null;
        $pdo = null;

        return true;
    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function isValidPostNo($postNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT COUNT(*) as cnt, status FROM coronaPost WHERE postNo = ?;";
        $st = $pdo->prepare($query);
        $st->execute([$postNo]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $st = null;
        $pdo = null;

        if ($res[0]['cnt'] == 0 || $res[0]['status'] != "ACTIVATE") {
            return -1;
        } else {
            return 1;
        }
    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function insertComment($postNo, $userNo, $content)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "INSERT INTO coronaComment (postNo, userNo, comment) VALUES (?,?,?); ";
        $st = $pdo->prepare($query);
        $st->bindParam(1, $postNo, PDO::PARAM_INT);
        $st->bindParam(2, $userNo, PDO::PARAM_INT);
        $st->bindParam(3, $content, PDO::PARAM_STR);

        $queryLikeCount = "UPDATE coronaPost SET commentCount = commentCount+1 WHERE postNo = ?;";
        $st2 = $pdo->prepare($queryLikeCount);
        $st2->bindParam(1, $postNo, PDO::PARAM_INT);


        $pdo->beginTransaction();
        $st->execute();
        $st2->execute();
        $pdo->commit();

        $st = null;
        $pdo = null;

        return true;
    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
        return 999;
    }

}

function getCoronaPost($page)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT postNo, title, content, likeCount, commentCount, createdAt, htmlContent,imageUrl FROM coronaPost WHERE status = 'ACTIVATE' ORDER BY createdAt DESC LIMIT ?, 20;";
        $st = $pdo->prepare($query);
        $st->bindParam(1, $page, PDO::PARAM_INT);
        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $st = null;
        $pdo = null;

        return $res;
    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function getCoronaHotPost()
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT postNo, title, content, likeCount, commentCount, createdAt, type, htmlContent,imageUrl FROM coronaPost WHERE status = 'ACTIVATE' ORDER BY type DESC , likeCount DESC LIMIT 5;";
        $st = $pdo->prepare($query);
        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $st = null;
        $pdo = null;

        return $res;
    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function getCoronaPostDetail($postNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT postNo, title, content, likeCount, commentCount, createdAt, htmlContent, imageUrl FROM coronaPost WHERE status = 'ACTIVATE' AND postNo = ?;";
        $st = $pdo->prepare($query);
        $st->execute([$postNo]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $st = null;
        $pdo = null;

        return $res[0];
    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function getCoronaComment($postNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT commentNo, comment, createdAt FROM coronaComment WHERE postNo = ? AND status = 'ACTIVATE' ORDER BY createdAt DESC;";
        $st = $pdo->prepare($query);
        $st->execute([$postNo]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $st = null;
        $pdo = null;

        return $res;
    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function updateLike($postNo, $userNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "INSERT INTO coronaLike(postNo, userNo) VALUES (?,?);";
        $st = $pdo->prepare($query);
        $st->bindParam(1, $postNo, PDO::PARAM_INT);
        $st->bindParam(2, $userNo, PDO::PARAM_INT);

        $queryLikeCount = "UPDATE coronaPost SET likeCount = likeCount+1 WHERE postNo = ?;";
        $st2 = $pdo->prepare($queryLikeCount);
        $st2->bindParam(1, $postNo, PDO::PARAM_INT);

        $pdo->beginTransaction();
        $st->execute();
        $st2->execute();
        $pdo->commit();

        $st = null;
        $pdo = null;

        return true;
    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
        return 999;
    }

}

function isValidLike($postNo, $userNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT COUNT(*) as cnt FROM coronaLike WHERE postNo = ? AND userNo = ?;";
        $st = $pdo->prepare($query);
        $st->execute([$postNo, $userNo]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $st = null;
        $pdo = null;

        if ($res[0]['cnt'] == 0) { //좋아요 가능
            return 1;
        } else {//좋아요 불가능
            return -1;
        }
    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function sendPushAndLogging($title, $message, $fcmToken, $sendUserNo, $postNo)
{
    $data = array("title" => $title,
        "message" => $message,
        "postNo" => $postNo);
    $sendFcmResult = sendFcm($fcmToken, $data, "AAAAw4hsfUc:APA91bHlR3QuaJDjycWbFOqRNSLzkK-HTj7EBzHJRoi8PlGWGK8oY0815ER2q74HFgf3zn39JP7ZP24SGgDyXhW0uM2wUhfso4a_hA66EXQzx7OnZBHBd3wpeegux6cnvrvTiEAwJ3B9", "ANDROID");
    if ($sendFcmResult) {
        try {
            $pushedUserNo = getUserNoByToken($fcmToken);
            $pdo = pdoSqlConnect();
            $query = "INSERT INTO coronaCommunityPush(pushedUserNo, sendUserNo, message) VALUES (?,?,?);";
            $st2 = $pdo->prepare($query);
            $st2->bindParam(1, $pushedUserNo, PDO::PARAM_INT);
            $st2->bindParam(2, $sendUserNo, PDO::PARAM_INT);
            $st2->bindParam(3, $message, PDO::PARAM_STR);
//
            $st2->execute([$pushedUserNo, $sendUserNo, $message]);

            $st = null;
            $pdo = null;

            return true;
        } catch (Exception $e) {
            echo $e;
            return 999;
        }
    }
}

function getUserTokenByPost($postNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT fcmToken FROM coronaPost,coronaUser WHERE coronaPost.userNo = coronaUser.userNo AND coronaPost.postNo = ?;";
        $st = $pdo->prepare($query);
        $st->execute([$postNo]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $st = null;
        $pdo = null;

        return $res[0]['fcmToken'];
    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function getSponsor()
{
    $pdo = pdoSqlConnect();
    $query = "SELECT * FROM sponsor;";

    $st = $pdo->prepare($query);
    $st->execute([]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}

function getGraph()
{
    $pdo = pdoSqlConnect();
    $query = "SELECT * FROM coronaGraph ORDER BY `index`;";

    $st = $pdo->prepare($query);
    $st->execute([]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}

function getLastPushInfo()
{
    $pdo = pdoSqlConnect();
    $query = "SELECT * FROM coronaPushHistory ORDER BY pushNo DESC LIMIT 1;";

    $st = $pdo->prepare($query);
    $st->execute([]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res[0];
}

//푸시 보낼 수 있는 유저의 토큰목록 얻기 300개만
function getFcmTokenArr300($pushInfo)
{
    $pdo = pdoSqlConnect();
    $query = "SELECT * FROM coronaUser where pushNo < ? AND error is null LIMIT 300;";
    $st = $pdo->prepare($query);
    $st->execute([$pushInfo['pushNo']]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    return $res;
}

function isCanPushByPushNo($pushNo, $userNo)
{
    $pdo = pdoSqlConnect();
    $query = "SELECT pushNo FROM coronaUser WHERE userNo = ?;";

    $st = $pdo->prepare($query);
    $st->execute([$userNo]);
    $st->setFetchMode(PDO::FETCH_ASSOC);
    $res = $st->fetchAll();

    $st = null;
    $pdo = null;

    if ($res[0]['pushNo'] < $pushNo) {
        return 1;
    } else {
        return 999;
    }
}

function sendPushMessageForScheduler($fcmTokenArr, $pushInfo)
{
    try {
        $count = 0;
        $pushNo = $pushInfo['pushNo'];

        if ($pushInfo['check']) {//진짜 푸시보내기
            if ($pushInfo['postNo'] > 0) {
                $data = array("title" => $pushInfo['title'],
                    "message" => $pushInfo['message'],
                    "postNo" => $pushInfo['postNo'],
                    "type" => $pushInfo['type']);
            } else {
                $data = array("title" => $pushInfo['title'],
                    "message" => $pushInfo['message'],
                    "type" => $pushInfo['type']);
            }
            echo json_encode($fcmTokenArr, JSON_NUMERIC_CHECK);
            foreach ($fcmTokenArr as $row) {
                $fcmToken = $row['fcmToken'];
                $userNo = $row['userNo'];
                if (isCanPushByPushNo($pushNo, $userNo) == 1) { // 이미 해당 푸시를 보냇는지 확인, 보낼수잇으면
                    echo json_encode($userNo);
                    $sendFcmResult = sendFcm($fcmToken, $data, "AAAAw4hsfUc:APA91bHlR3QuaJDjycWbFOqRNSLzkK-HTj7EBzHJRoi8PlGWGK8oY0815ER2q74HFgf3zn39JP7ZP24SGgDyXhW0uM2wUhfso4a_hA66EXQzx7OnZBHBd3wpeegux6cnvrvTiEAwJ3B9", "ANDROID");
                    if ($sendFcmResult['success']) {
                        updateLastPushAt($userNo, $pushNo);
                        $count++;
                    } else {
                        updatePushError($fcmToken, $sendFcmResult['results'][0]['error']);
                    }
                }
            }
            return $count;
        } else {//확인필요
            echo "푸시확인필요";
        }
    } catch (Exception $e) {
        echo $e;
        return 0;
    }


}

function isValidDeclaration($postNo, $userNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT COUNT(*) as cnt FROM coronaDeclaration WHERE postNo = ? AND userNo = ?;";
        $st = $pdo->prepare($query);
        $st->execute([$postNo, $userNo]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $st = null;
        $pdo = null;

        if ($res[0]['cnt'] == 0) { //신고 가능
            return 1;
        } else {//신고 불가능
            return -1;
        }
    } catch (Exception $e){
        echo $e;
        return 999;
    }

}

function updateDeclaration($postNo, $userNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "INSERT INTO coronaDeclaration(postNo, userNo) VALUES (?,?);";
        $st = $pdo->prepare($query);
        $st->bindParam(1, $postNo, PDO::PARAM_INT);
        $st->bindParam(2, $userNo, PDO::PARAM_INT);

        $queryDeclarationCount = "UPDATE coronaPost SET declarationCount = declarationCount+1 WHERE postNo = ?;";
        $st2 = $pdo->prepare($queryDeclarationCount);
        $st2->bindParam(1, $postNo, PDO::PARAM_INT);

        if(getDeclarationCount($postNo) > 3){
            $queryPostDeclaration = "UPDATE coronaPost SET status = 'DECLARATION' WHERE postNo = ?;";
            $st3 = $pdo->prepare($queryPostDeclaration);
            $st3->bindParam(1, $postNo, PDO::PARAM_INT);

            $pdo->beginTransaction();
            $st->execute();
            $st2->execute();
            $st3->execute();
            $pdo->commit();
        }
        else{
            $pdo->beginTransaction();
            $st->execute();
            $st2->execute();
            $pdo->commit();
        }


        $st = null;
        $pdo = null;

        return true;
    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
        return 999;
    }

}

function getDeclarationCount($postNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT declarationCount FROM coronaPost WHERE postNo = ?";
        $st = $pdo->prepare($query);
        $st->execute([$postNo]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $st = null;
        $pdo = null;

        return $res[0]['declarationCount'];
    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}
