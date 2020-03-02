<?php
require 'function.php';

const JWT_SECRET_KEY = "TEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEYTEST_KEY";
const FCM_SERVER_KEY = "AAAAlvHpLX0:APA91bFragmSbcQL3AxalcKWt0rtp-8TXcvvSnQIWWleoMJlzqtAZhfYmqo3CvuKi5aFMDL2COTKVMO4feMd_dSQQ-4jhv0DlLUddpJ7TicFkjaSa-QcR0KZTNiDofXDc8SypOZUpiow";

$res = (Object)Array();
header('Content-Type: json');
$req = json_decode(file_get_contents("php://input"));
try {
    addAccessLogs($accessLogs, $req);
    switch ($handler) {
        /*
         * API No. 0
         * API Name : JWT 유효성 검사 테스트 API
         * 마지막 수정 날짜 : 19.04.25
         */
        case "home":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $page = $req->page;
            $size = $req->size;
            $result = $req->result;

            $count = 0;
            foreach ($result as $nationalNo => $value) {
                $nationalId = $value->nationalNo;
                $national[$count++] = $nationalId;
            }

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (count($national) < 1) {
                    $res->isSuccess = false;
                    $res->code = 112;
                    $res->message = "관심지역을 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($page) < 1 or strlen($size) < 1) {
                    $res->isSuccess = false;
                    $res->code = 116;
                    $res->message = "페이징과 사이즈를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($nationalNo) > 0 and strlen($page) > 0 and strlen($size) > 0) {
                    $res->result = getHome($national, $userNo, $page, $size);
                    $res->code = 100;
                    $res->message = "피드조회 성공";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                }
            }
            break;


        case "postPost":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];
            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $nationalNo = $req->nationalNo;
            $text = $req->text;
            $photo = $req->photo;

            $count = 0;
            foreach ($photo as $url => $value) {
                $urlResult = $value->url;
                $photoResult[$count++] = $urlResult;
            }

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {

                if (strlen($text) < 1) {
                    $res->isSuccess = false;
                    $res->code = 117;
                    $res->message = "글내용을 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($nationalNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 118;
                    $res->message = "지역번호을 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (count($photo) < 1 and strlen($text) > 0 and strlen($nationalNo) > 0) {
                    http_response_code(200);
                    without_postPost($userNo, $nationalNo, $text);
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "게시글 저장을 성공했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (count($photo) > 0 and strlen($text) > 0 and strlen($nationalNo) > 0) {
                    http_response_code(200);
                    postPost($userNo, $nationalNo, $text, $photoResult);
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "게시글 저장을 성공했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }
            }
            break;

        case "getPost":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $postNo = $vars["postNo"];

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (strlen($postNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 100;
                    $res->message = "글번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($postNo) > 0) {
                    $res->result = getPost($postNo, $userNo);
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "게시글 상세조회를 성공했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }
            }

            break;

        case "getComments": //댓글 조회 API 댓글만 조회함

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $postNo = $vars["postNo"];

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (strlen($postNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 144;
                    $res->message = "글번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($postNo) > 0) {
                    $res->result = getCommnets($postNo);
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "댓글 조회를 성공했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);

                    return;
                }
            }

            break;

        case "postLike":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $postNo = $vars["postNo"];

            $pattern_postNo = "/^[0-9]+$/";

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {

                if (!preg_match($pattern_postNo, $postNo)) {
                    $res->isSuccess = false;
                    $res->code = 120;
                    $res->message = "게시글 번호를 숫자로 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($postNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 119;
                    $res->message = "게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $isalreadyLike = likeCheck($userNo, $postNo);

                if ($isalreadyLike == 1) {
                    $res->isSuccess = false;
                    $res->code = 121;
                    $res->message = "이미 좋아요한 게시글 입니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($postNo) > 0) {
                    postLike($postNo, $userNo);
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "좋아요를 성공했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);

                    $myNickname = getNickname($userNo);
                    $toUserNo = getUserNoFromPost($postNo);

                    if ($toUserNo == $userNo) {
                        return;
                    }
                    $data = array("title" => "히어데어",
                        "body" => $myNickname . "님이 내 글에 좋아요를 눌렀습니다",
                        "message" => $myNickname . "님이 내 글에 좋아요를 눌렀습니다",
                        "postNo" => $postNo);

//                    echo $toUserNo;
                    $tokens = getFCMTokens($toUserNo);
                    $key = $tokens;
                    if (isAcceptedPush($toUserNo) && !isLogout($toUserNo)) {
                        $result = sendFcm($key, $data, FCM_SERVER_KEY, 'ANDROID');
                        if ($result == 1) {
                            insertPushHistory((int)$userNo, (int)$toUserNo, 1, $postNo);
                        }
                    }
                    return;
                }
            }

            break;

        case "deleteLike":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $postNo = $vars["postNo"];

            $pattern_postNo = "/^[0-9]+$/";

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {

                if (!preg_match($pattern_postNo, $postNo)) {
                    $res->isSuccess = false;
                    $res->code = 123;
                    $res->message = "게시글 번호를 숫자로 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($postNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 122;
                    $res->message = "게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $isalreadyLike = likeCheck($userNo, $postNo);

                if ($isalreadyLike == 0) {
                    $res->isSuccess = false;
                    $res->code = 124;
                    $res->message = "좋아요가 되어있지 않은 게시글 입니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($postNo) > 0) {
                    deleteLike($userNo, $postNo);
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "좋아요 해제를 성공했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }
            }
            break;

        case "doScrap_post":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $postNo = $vars["postNo"];
            $scrapNo = $vars["scrapNo"];
            $pattern_No = "/^[0-9]+$/";

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (!preg_match($pattern_No, $postNo)) {
                    $res->isSuccess = false;
                    $res->code = 128;
                    $res->message = "게시글 번호를 숫자로 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (!preg_match($pattern_No, $scrapNo)) {
                    $res->isSuccess = false;
                    $res->code = 129;
                    $res->message = "스크랩북 번호를 숫자로 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($postNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 130;
                    $res->message = "게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($scrapNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 131;
                    $res->message = "스크랩북 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $isexistScrap = scrapCheck($userNo, $scrapNo);
                if ($isexistScrap == 0) {
                    $res->isSuccess = false;
                    $res->code = 132;
                    $res->message = "유효한 스크랩북 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }
                $isexistPost = postCheck($postNo, $scrapNo);
                if ($isexistPost == 0) {
                    $res->isSuccess = false;
                    $res->code = 133;
                    $res->message = "유효한 게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($postNo) > 0 and strlen($scrapNo) > 0) {

                    $isexistScrappost = postScrapCheck($userNo, $postNo);

                    if ($isexistScrappost == 1) {
                        $res->isSuccess = false;
                        $res->code = 134;
                        $res->message = "이미 스크랩한 게시글";
                        echo json_encode($res, JSON_NUMERIC_CHECK);
                        return;
                    }

                    http_response_code(200);
                    doScrap($scrapNo, $postNo, $userNo);
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "스크랩을 성공했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);


                    $myNickname = getNickname($userNo);
                    $toUserNo = getUserNoFromPost($postNo);

                    if ($toUserNo == $userNo) {
                        return;
                    }

                    $data = array("title" => "히어데어",
                        "body" => $myNickname . "님이 내 글을 스크랩했습니다.",
                        "message" => $myNickname . "님이 내 글을 스크랩했습니다.",
                        "postNo" => $postNo);

//                    echo $toUserNo;
                    $tokens = getFCMTokens($toUserNo);
                    $key = $tokens;

                    if (isAcceptedPush($toUserNo) && !isLogout($toUserNo)) {
                        $result = sendFcm($key, $data, FCM_SERVER_KEY, 'ANDROID');
                        if ($result == 1) {
                            insertPushHistory((int)$userNo, (int)$toUserNo, 3, $postNo);
                        }
                    }
                    return;
                }
            }
            break;

        case "deleteScrap_post":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $postNo = $vars["postNo"];
            $scrapNo = $vars["scrapNo"];
            $pattern_No = "/^[0-9]+$/";

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (!preg_match($pattern_No, $postNo)) {
                    $res->isSuccess = false;
                    $res->code = 134;
                    $res->message = "게시글 번호를 숫자로 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (!preg_match($pattern_No, $scrapNo)) {
                    $res->isSuccess = false;
                    $res->code = 135;
                    $res->message = "스크랩북 번호를 숫자로 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($postNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 136;
                    $res->message = "게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($scrapNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 137;
                    $res->message = "스크랩북 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $isexistScrap = scrapCheck($userNo, $scrapNo);

                if ($isexistScrap == 0) {
                    $res->isSuccess = false;
                    $res->code = 138;
                    $res->message = "유효한 스크랩북 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $isexistPost = postCheck($postNo);

                if ($isexistPost == 0) {
                    $res->isSuccess = false;
                    $res->code = 139;
                    $res->message = "유효한 게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($postNo) > 0 and strlen($scrapNo) > 0) {
                    http_response_code(200);
                    dontScap($postNo, $scrapNo);
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "스크랩 해제를 성공했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }
            }

            break;

        case "deleteScrap_post_no_scrap":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $postNo = $vars["postNo"];
            $scrapNo = $vars["scrapNo"];
            $pattern_No = "/^[0-9]+$/";

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (!preg_match($pattern_No, $postNo)) {
                    $res->isSuccess = false;
                    $res->code = 134;
                    $res->message = "게시글 번호를 숫자로 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($postNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 136;
                    $res->message = "게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }


                $isexistPost = postCheck($postNo);

                if ($isexistPost == 0) {
                    $res->isSuccess = false;
                    $res->code = 139;
                    $res->message = "유효한 게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $isScrapedPost = isScrapedPost($userNo, $postNo);

                if ($isScrapedPost == 0) {
                    $res->isSuccess = false;
                    $res->code = 137;
                    $res->message = "스크랩 하지 않은 게시물입니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }


                $scrapDeleteResult = dontScapByUserNo($postNo, $userNo);

                if ($scrapDeleteResult == 1) {
                    http_response_code(200);
                    dontScap($postNo, $scrapNo);
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "스크랩 해제를 성공했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                } else {
                    $res->isSuccess = FALSE;
                    $res->code = 200;
                    $res->message = "스크랩 해제에 실패했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                }
            }

            break;

        case "postComments":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $postNo = $vars["postNo"];
            $content = $req->content;
            $tagNo = $req->tagNo;

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (strlen($content) < 1) {
                    $res->isSuccess = false;
                    $res->code = 145;
                    $res->message = "글내용을 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($tagNo) < 1) {
                    postComments_without_tag($userNo, $postNo, $content);
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "댓글 작성을 성공했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);

                    $myNickname = getNickname($userNo);
                    $toUserNo = getUserNoFromPost($postNo);

                    if ($toUserNo == $userNo) {
                        return;
                    }

                    $data = array("title" => "히어데어",
                        "body" => $myNickname . "님이 내 글에 댓글을 달았습니다",
                        "message" => $myNickname . "님이 내 글에 댓글을 달았습니다",
                        "postNo" => $postNo);
                    $tokens = getFCMTokens($toUserNo);
                    $key = $tokens;

                    if (isAcceptedPush($toUserNo) && !isLogout($toUserNo)) {
                        $result = sendFcm($key, $data, FCM_SERVER_KEY, 'ANDROID');
                        if ($result == 1) {
                            insertPushHistory((int)$userNo, (int)$toUserNo, 2, $postNo);
                        }
                    }
                    return;
                } else if (strlen($tagNo) > 0 and strlen($content) > 0) {
                    postComments($userNo, $postNo, $content, $tagNo);
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "댓글 작성을 성공했습니다";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
//                    echo $postNo;

                    $myNickname = getNickname($userNo);
                    $toUserNo = getUserNoFromComment($tagNo);

                    if ($toUserNo == $userNo) {
                        return;
                    }

                    $data = array("title" => "히어데어",
                        "body" => $myNickname . "님이 내 댓글에 댓글을 달았습니다",
                        "message" => $myNickname . "님이 내 댓글에 댓글을 달았습니다",
                        "postNo" => $postNo,
                        "commentNo" => $tagNo);
                    $tokens = getFCMTokens($toUserNo);
                    $key = $tokens;

                    if (isAcceptedPush($toUserNo) && !isLogout($toUserNo)) {

                        $result = sendFcm($key, $data, FCM_SERVER_KEY, 'ANDROID');
                        if ($result == 1) {
                            insertPushHistory((int)$userNo, (int)$toUserNo, 4, $postNo);
                        }
                    }
                    return;
                }
            }

            break;

        case "getBundle":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $postNo = $vars["postNo"]; //commentsNo
            $commentsNo = $vars["commentsNo"];
            $tagNo = $_GET['tagNo'];

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (strlen($commentsNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 146;
                    $res->message = "댓글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($tagNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 146;
                    $res->message = "태그 댓글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $res->result = getTagcomments($commentsNo, $tagNo);
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "댓글 조회를 성공했습니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            break;

        case "getscrapHome":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $scrapNo = $vars["scrapNo"];
            $page = (int)$vars["page"];

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (strlen($scrapNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 147;
                    $res->message = "스크랩 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $res->result = getscrapHome($scrapNo, $userNo, $page);
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "스크랩북 피드 조회를 성공했습니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;

            }

            break;

        case "searchPost":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $word = $req->word;
            $page = $req->page;

            $result = $req->result;

            $count = 0;
            foreach ($result as $nationalNo => $value) {
                $nationalId = $value->nationalNo;
                $national[$count++] = $nationalId;
            }


            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (strlen($word) < 1) {
                    $res->isSuccess = false;
                    $res->code = 147;
                    $res->message = "검색어를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (count($result) < 1) {
                    $res->isSuccess = false;
                    $res->code = 148;
                    $res->message = "지역번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $res->result = getPost_word($userNo, $national, $word, $page);
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "게시글 검색을 성공했습니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;

            }
            break;


        case "searchNick":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $word = $req->word;
            $page = $req->page;

            $userNo = convert_to_userNo($email);


            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (strlen($word) < 1) {
                    $res->isSuccess = false;
                    $res->code = 147;
                    $res->message = "검색어를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $res->result = getnick($word, $page, $userNo);
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "닉네임 검색을 성공했습니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            break;

        case "patchPost":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $postNo = $vars["postNo"];
            $photo = $req->photo;
            $text = $req->text;
            $nationalNo = $req->nationalNo;

            $pattern_postNo = "/^[0-9]+$/";

            $userNo = convert_to_userNo($email);

            $count = 0;
            foreach ($photo as $url => $value) {
                $urlResult = $value->url;
                $photoResult[$count++] = $urlResult;
            }

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (count($text) < 1) {
                    $res->isSuccess = false;
                    $res->code = 144;
                    $res->message = "글내용을 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($nationalNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 147;
                    $res->message = "지역번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (strlen($postNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 147;
                    $res->message = "게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (!preg_match($pattern_postNo, $postNo)) {
                    $res->isSuccess = false;
                    $res->code = 120;
                    $res->message = "게시글 번호를 숫자로 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $isexistPost = postCheck($postNo);

                if ($isexistPost == 0) {
                    $res->isSuccess = false;
                    $res->code = 139;
                    $res->message = "유효한 게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                patchPost($postNo, $userNo, $photoResult, $nationalNo, $text);
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "게시글 수정을 성공했습니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            break;

        case "deletePost":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $postNo = $vars["postNo"];

            $pattern_postNo = "/^[0-9]+$/";

            $userNo = convert_to_userNo($email);

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                if (strlen($postNo) < 1) {
                    $res->isSuccess = false;
                    $res->code = 147;
                    $res->message = "게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                if (!preg_match($pattern_postNo, $postNo)) {
                    $res->isSuccess = false;
                    $res->code = 148;
                    $res->message = "게시글 번호를 숫자로 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                $imageNoList = getPostImageNo($postNo);
//                echo json_encode($imageNoList, JSON_NUMERIC_CHECK);

                $isexistPost = postCheck($postNo);

                if ($isexistPost == 0) {
                    $res->isSuccess = false;
                    $res->code = 149;
                    $res->message = "유효한 게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }

                deletePost($postNo, $userNo, $imageNoList);
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "게시글 삭제를 성공했습니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;

            }

            break;

        case "autoLogin":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];

            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];


            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                $res->isSuccess = TRUE;
                $res->code = 100;
                $res->message = "자동로그인 성공";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                return;
            }

            break;

        case "testPostNo":

            $jwt = $_SERVER["HTTP_X_ACCESS_TOKEN"];
            $result = isValidHeader($jwt, JWT_SECRET_KEY);
            $isintval = $result['intval'];
            $email = $result['email'];
            $postNo = $vars["postNo"];

            if ($isintval === 0) //토큰 검증 여부
            {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req); //에러로그 오류
                return;
            } else if ($isintval === 1) {
                $isexistPost = postCheck($postNo);

                if ($isexistPost == 0) {
                    $res->isSuccess = false;
                    $res->code = 149;
                    $res->message = "유효한 게시글 번호를 입력해주세요";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                } else if ($isexistPost == 1) {
                    $res->isSuccess = TRUE;
                    $res->code = 100;
                    $res->message = "게시글 유효함";
                    echo json_encode($res, JSON_NUMERIC_CHECK);
                    return;
                }
            }

            break;


        case "validateJwt":
            // jwt 유효성 검사
            if (!isValidHeader($jwt, JWT_SECRET_KEY)) {
                $res->isSuccess = FALSE;
                $res->code = 201;
                $res->message = "유효하지 않은 토큰입니다";
                echo json_encode($res, JSON_NUMERIC_CHECK);
                addErrorLogs($errorLogs, $res, $req);
                return;
            }

            http_response_code(200);
            $res->isSuccess = TRUE;
            $res->code = 100;
            $res->message = "테스트 성공";

            echo json_encode($res, JSON_NUMERIC_CHECK);
            break;
        /*
         * API No. 0
         * API Name : JWT 생성 테스트 API
         * 마지막 수정 날짜 : 19.04.25
         */
    }
} catch (\Exception $e) {
    return getSQLErrorException($errorLogs, $e, $req);
}
