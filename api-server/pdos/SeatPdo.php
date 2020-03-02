<?php
function getSeat()
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT * FROM seat ";
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

function postSeat($seatNo, $status)
{
//    echo $seatNo, $status;
    try {
        $pdo = pdoSqlConnect();
        if ($status == 0) {
            $query = "UPDATE seat SET status = ?, sound = 0 WHERE seatNo= ?;";
        } else {
            $query = "UPDATE seat SET status = ? WHERE seatNo= ?;";
        }
        $st = $pdo->prepare($query);

        $st->bindParam(1, $status, PDO::PARAM_INT);
        $st->bindParam(2, $seatNo, PDO::PARAM_INT);

        $st->execute();


        $st = null;
        $pdo = null;

        $res = (Object)Array();
        $res->code = 100;
        $res->message = "update success";

        echo json_encode($res, JSON_NUMERIC_CHECK);


    } catch (Exception $e) {
        echo $e;
//        return 999;
    }

}

function soundOff($seatNo)
{
//    echo $seatNo, $status;
    try {
        $pdo = pdoSqlConnect();

        $query = "UPDATE seat SET sound = 0 WHERE seatNo= ?;";

        $st = $pdo->prepare($query);

        $st->bindParam(1, $seatNo, PDO::PARAM_INT);

        $st->execute();


        $st = null;
        $pdo = null;

        $res = (Object)Array();
        $res->code = 100;
        $res->message = "update success";

        echo json_encode($res, JSON_NUMERIC_CHECK);


    } catch (Exception $e) {
        echo $e;
//        return 999;
    }

}

function signUpRb($id, $password, $url, $name)
{
    try {
        $pdo = pdoSqlConnect();
        $pdo->beginTransaction();

        $query = "INSERT INTO rbUser (id, password, url, name) VALUES (?,?,?,?);";


        $st = $pdo->prepare($query);
        $st->bindParam(1, $id, PDO::PARAM_STR);
        $st->bindParam(2, $password, PDO::PARAM_STR);
        $st->bindParam(3, $url, PDO::PARAM_STR);
        $st->bindParam(4, $name, PDO::PARAM_STR);

        $st->execute();

        $pdo->commit();

        return 1;

    } catch (Exception $e) {
        if ($pdo->inTransaction()) {
            $pdo->rollback();
        }
        echo $e;
        return 2;
    }

}

function getUserLb()
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT * FROM rbUser";
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

function verifyUser($id)
{
//    echo $seatNo, $status;
    try {
        $pdo = pdoSqlConnect();
        $query = "UPDATE rbUser SET isVerify = 1 WHERE userNo = ?;";
        $st = $pdo->prepare($query);

        $st->bindParam(1, $id, PDO::PARAM_STR);

        $st->execute();


        $st = null;
        $pdo = null;

        return 1;

    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function reservationDone($seatNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "UPDATE seat SET status = 2 WHERE seatNo= ?;";
        $st = $pdo->prepare($query);

        $st->bindParam(1, $seatNo, PDO::PARAM_STR);

        $st->execute();


        $st = null;
        $pdo = null;

        return 1;

    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}


function getUserLbDetail($id)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT * FROM rbUser WHERE userNo = ?";
        $st = $pdo->prepare($query);
        $st->execute([$id]);
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

function reservationRequest($seatNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "UPDATE seat SET status = 3 WHERE seatNo= ?;";
        $st = $pdo->prepare($query);

        $st->bindParam(1, $seatNo, PDO::PARAM_STR);

        $st->execute();


        $st = null;
        $pdo = null;

        return 1;

    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function soundRequest($seatNo)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "UPDATE seat SET sound = 1 WHERE seatNo= ?;";
        $st = $pdo->prepare($query);

        $st->bindParam(1, $seatNo, PDO::PARAM_STR);

        $st->execute();


        $st = null;
        $pdo = null;

        return 1;

    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function getUserVerify($id)
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT * FROM rbUser WHERE id = ?";
        $st = $pdo->prepare($query);
        $st->execute([$id]);
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $st = null;
        $pdo = null;

        if ($res[0]['isVerify'] == 1) {
            return true;
        } else {
            return false;
        }

    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function pushTimeCheck()
{
    try {
        $pdo = pdoSqlConnect();
        $query = "SELECT TIMESTAMPDIFF(second, (SELECT pushTime FROM push_time) , NOW()) AS time_diff ;";
        $st = $pdo->prepare($query);
        $st->execute();
        $st->setFetchMode(PDO::FETCH_ASSOC);
        $res = $st->fetchAll();
        $st = null;
        $pdo = null;

        return $res[0]['time_diff'];

    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}

function pushTimeUpdate()
{
    try {
        $pdo = pdoSqlConnect();
        $query = "UPDATE push_time SET pushTime = NOW();";
        $st = $pdo->prepare($query);
        $st->execute();
        
    } catch (Exception $e) {
        echo $e;
        return 999;
    }

}