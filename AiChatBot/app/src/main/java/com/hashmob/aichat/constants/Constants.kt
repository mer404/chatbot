package com.hashmob.aichat.constants

import android.os.Environment

object Constants {
    const val MESSAGE_TYPE = "TEXT"
    const val DEFAULT_ZERO = 0
    const val DEFAULT_ONE = 1
    const val API_TIME: Long = 30000

    /**
     * RUN TIME PERMISSION REQUEST CODES
     */
    const val REQUEST_CODE_CAMERA_PERMISSION = 110
    const val REQUEST_CODE_STORAGE_PERMISSION = 111
    const val REQUEST_CODE_CALL_PERMISSION = 333
    const val REQUEST_CODE_LOCATION = 333
    const val FILE_REQUEST_CODE = 100
    const val REQUEST_CODE_GPS_PERMISSION = 113

    /**
     * START ACTIVITY FOR RESULT CODE
     */
    const val REQUEST_CODE_SEARCH_CONTACTS = 501
    const val REQUEST_CODE_SPEECH_INPUT = 1

    /**
     * PERMISSION RESULT CODE
     */
    const val REQUEST_CODE_PERMISSION_RESULT = 600
    const val REQUEST_CODE_CAMERA_STORAGE_RESULT = 601

    /**
     * FRAGMENT FLAGS AND TAGS
     */
    const val START_DRAWER_FRAGMENT_TAG = "StartDrawerFragment"
    const val START_DRAWER_FRAGMENT_FLAG = 1

    /**
     * DATE/TIME FORMAT
     */
    const val DATE_SOURCE_FORMAT = "yyyy/MM/dd"
    const val DATE_DD_MM_YYYY_FORMAT = "dd/MM/yyyy"
    const val DATE_DD_MMM_YYYY_FORMAT = "dd MMM yyyy"
    const val DATE_DD_MM_YYYY_HH_MM_AA_FORMAT = "dd/MM/yyyy hh:mm a"
    const val DATE_DD_MM_YYYY_HH_MM_AA_FORMAT_ = "dd MMM yyyy hh:mm aa"
    const val DATE_YYYY_MM_DD_HH_MM_AA_FORMAT = "yyyy/MM/dd HH:mm:ss"
    const val DATE_YYYY_MM_DD_FORMAT = "yyyy-MM-dd"
    const val DATE_YYYY_MM_DD_HH_FORMAT = "yyyy-MM-dd HH"
    const val DATE_D_MMMM_FORMAT = "d MMMM"
    const val DATE_HH_FORMAT = "HH"
    const val TIME_HH_MM_SS_FORMAT = "HH:mm:ss"
    const val TIME_HH_AA_DD_MMMM_FORMAT = "ha 'on' dd MMMM yyyy"
    const val TIME_HH_MM_A_FORMAT = "hh:mm a"
    const val DATE_PUSH_NOTIFICATION_FORMAT = "HHmmssSSS"
    const val IMAGE_FILE_NAME_PREFIX = "IMG_CAM" + "_X" + ".jpg"
    const val VIDEO_FILE_NAME_PREFIX = "VID_CAM" + "_X" + ".jpg"
    const val AUDIO_NAME_PREFIX = "AUDIO" + "_X" + ".mp3"
    const val FLAG_CROP = 1314
    const val DATE_FILE_FORMAT = "HHmmssSSS"

    /**
     * INTENT FLAGS
     */
    var from = "from"
    var from_push = "from_push"
    var from_chat_push = "from_chat_push"
    var from_add = 1
    var from_edit = 2
    var from_home = 1
    var from_connection = 2
    var from_registration = "from_registration"
    var from_login = "from_login"
    var LANGUAGE = "English"
    var LENGTH = "Small"
    var url = "url"
    var topic = "topic"
    var topicLength = "topicLength"
    var WHICH = "WHICH"
    var web_view = "web_view"
    var LANGUAGE_POSITION = "LANGUAGE_POSITION"
    var LENGTH_POSITION = "LENGTH_POSITION"
    var TOPIC_NAME = "TOPIC_NAME"
    var SETTING_FLAG = "SETTING_FLAG"
    var SUBTITLE = "SUBTITLE"
    var answer = "answer"

    /**
     * ITEM CLICKS FLAGS FOR INTERACT
     */
    const val ITEM_CLICK = 0
    const val ITEM_CLICK_CATEGORY = 60
    const val ITEM_CLICK_PRODUCT = 61
    const val ITEM_CLICK_DELETE = 62
    const val MIN_AGE = 18
    const val MAX_AGE = 60
    var SELECT_PROMPT = 0
    var isProVersion = "isProVersion"
    var pro_version = 1
    var not_pro_version = 0


    /**
     * DEVICE TYPE PASS IN API
     */
    const val DEVICE_TYPE_ANDROID = "0"

    //default list load time duration
    const val TIME_DURATION = 3000
    const val offset = "offset"
    const val FROM_NORMAL = 0
    const val FROM_PULL_REFRESH = 1

    /**
     * USER DATA
     */
    const val user_gson = "user_gson"
    const val user_id = "user_id"
    const val access_token = "access_token"
    const val auth_token = "auth_token"
    const val DATA = "data"
    const val image_id = "image_id"
    const val mood_rate = "mood_rate"
    const val mood_msg = "mood_msg"
    const val mood_json = "mood_json"
    const val facebook = "facebook"
    const val config_test = "config"
    const val config = "config"
    const val isFirstTime = "isFirstTime"
    const val remote_data = "remote_data"
    const val appData = "appData"
    const val allService = "allService"
    const val generateImageService = "generateImageService"
    const val firstTimeUsedTimestamp = "firstTimeUsedTimestamp"
    const val appLanguageData = "appLanguageData"
    const val appPromptData = "appPromptData"
    const val updateFirebaseJson = "updateFirebaseJson"
    const val totalCoin = "totalCoin"

    /**
     * API parameters
     */
    const val name = "name"
    const val prompt = "prompt"
    const val n = "n"
    const val size = "size"
    const val temperature = "temperature"
    const val model = "model"
    const val max_tokens = "max_tokens"
    const val top_p = "top_p"
    const val frequency_penalty = "frequency_penalty"
    const val presence_penalty = "presence_penalty"
    const val stop = "stop"
    const val phone = "phone"
    const val email = "email"
    const val gender = "gender"
    const val about = "about"
    const val country_code = "country_code"
    const val mobile_no = "mobile_no"
    const val phone_number = "phone_number"
    const val password = "password"
    const val emoji_id = "emoji_id"
    const val mood_score = "mood_score"
    const val is_send_mail = "is_send_mail"
    const val is_like = "is_like"
    const val mood_date = "mood_date"
    const val mood_time = "mood_time"
    const val mood_description = "mood_description"
    const val description = "description"
    const val start_date = "start_date"
    const val end_date = "end_date"
    const val contact_id = "contact_id"
    const val contact_data = "contact_data"
    const val conatct_is_edit = "conatct_is_edit"
    const val messages = "messages"
    const val role = "role"
    const val content = "content"
    const val role_system = "system"
    const val role_user = "user"
    const val scan_image_model = "gpt-4-vision-preview"

    /**
     * DEFAULT CONSTANTS
     */
    var PERMISSION = 0
    const val DESIGN_IMAGE_RADIUS = 10
    const val IMAGE_COUNTER = "IMAGE_COUNT"
    const val PRO_IMAGE_COUNTER = "PRO_IMAGE_COUNT"

    /**
     * Language localisation
     */
    var language = "language"
    var language_en = "en"
    var language_english = "en"
    var language_ar = "ar"
    var language_arabic = "ar"

    /**
     * IMAGE/VIDEO
     */
    var APP_ROOT_FOLDER = Environment
        .getExternalStorageDirectory()
        .absolutePath + "/" + "Mentabee" + "/"
    val IMAGE_ROOT_FOLDER = APP_ROOT_FOLDER + "IMAGES"
    val APP_DOWNLOADS_FOLDER = APP_ROOT_FOLDER + "MEDIA"
    val AUDIO_ROOT_FOLDER = APP_ROOT_FOLDER + "AUDIO"

    /**
     * CROP
     */
    @JvmField
    var image = "image"
    var imageArray = "imageArray"
    var video = "video"
    var video_url = "video_url"

    @JvmField
    var FLAG_IS_SQUARE = "flag_is_square"
    var IMAGE_RESIZE_WIDTH = 500
    var IMAGE_RESIZE_HEIGHT = 500
    var IMAGE_RESIZE_HEIGHT_NEW = 150
    var IMAGE_RESIZE_WIDTH_NEW = 150

    /**
     * FCM PUSH NOTIFICATION
     */
    var fcm_registration_id = "fcm_registration_id"
    var device_id = "device_token"
    var device_type = "device_type"
    var device_type_value = "android"
    var register_id = "register_id"
    var push_type = "push_type"
    var lang = "lang"
    var title = "title"
    var body = "body"
    var badge = "badge"
    var is_first_time = "is_first_time"

    /**
     * API RESPONSE FLAGS
     */
    var RESPONSE_FAILURE_FLAG = 0
    var RESPONSE_SUCCESS_FLAG = 1
    var RESPONSE_LOGOUT_FLAG = 2
    var RESPONSE_SOCIAL_FLAG = 2

    /**
     * PASSWORD LIMIT GLOBAL
     */
    var PASS_LIMIT = 6
    const val SPLASH_WAIT: Long = 1500

    /**
     * LIST PAGINATION
     */
    var pagination_start_offset = 1
    var pagination_last_offset = -1
    const val stayEmail = "stayEmail"
    const val stayPass = "stayPass"
    const val IMAGE_PREFIX_URL = "http://159.223.225.68/rusocial/app_images/user_profile/"
    const val verify_code = "verify_code"
    const val age = "age"
    const val bio = "bio"
    const val job_title = "job_title"
    const val min_age = "min_age"
    const val max_age = "max_age"
    const val verify_from = "verify_from"
    const val verify_phone = "verify_phone"
    const val latitude = "latitude"
    const val longitude = "longitude"
    const val page = "page"
    const val limit = "limit"
    const val user_data = "user_data"
    const val opo_user_token = "opo_user_token"
    const val message = "message"
    const val request_id = "request_id"
    const val request = "request"
    const val block_user_token = "block_user_token"
    const val block = "block"
    const val report_user_token = "report_user_token"
    const val conversation_id = "conversation_id"
    const val type = "type"
    const val chat_delete = "chat_delete"
    const val chat_id = "chat_id"
    const val oppo_user_token = "oppo_user_token"
    const val is_snooze = "is_snooze"
    const val files = "files"
    const val file = "file"
    const val user_images_id = "user_images_id"
    const val driving_mode = "driving_mode"
    const val text = "text"
    const val image_url = "image_url"

    //    public static final String min_age = "min_age";
    //    public static final String max_age = "max_age";
    const val file1 = "file1"
    const val file2 = "file2"
    const val old_password = "old_password"
    const val new_password = "new_password"
    const val device_token = "device_token"
    const val receiver_token = "receiver_token"
    const val chat_date = "chat_date"
    const val message_type = "message_type"
    const val chat_data = "chat_data"
    const val is_video = "is_video"
    var CONVERSATION_NORMAL = "NORMAL"
    const val is_purchased = "is_purchased"
    const val is_other_purchased = "is_other_purchased"
    const val is_android_purchased = "is_android_purchased"
    const val purchase_start_date_ms = "purchase_start_date_ms"
    const val purchase_end_date_ms = "purchase_end_date_ms"
    const val original_transaction_id = "original_transaction_id"
    const val receiptUrl = "receiptUrl"
    const val packageName = "packageName"
    const val productId = "productId"
    const val purchaseToken = "purchaseToken"
    const val product_identifier = "product_identifier"
    const val PURCHASED = 1
    const val NOT_PURCHASED = 2
    const val ERR_MESSAGE = 3
    const val AiChat = "/AiChat/"
    const val extensionPDF = ".pdf"
    const val SUBSCRIPTION_TEST_PER_MONTH = "subscription_test_month"
    const val SUBSCRIPTION_TEST_PER_WEEK = "subscription_test"

    //Events
    const val first_open = "first_open"
    const val login_start = "login_start"
    const val registar_start = "registar_start"
    const val verify_submit = "verify_submit"
    const val home_view = "home_view"
    const val user_selected = "user_selected"
    const val chat_view = "chat_view"
    const val profile_view = "profile_view"
    const val logout_start = "logout_start"
    const val logout_yes = "logout_yes"
    const val logout_no = "logout_no"
    const val save_profile = "save_profile"
    const val change_password = "change_password"
    const val delete_account_start = "delete_account_start"
    const val delete_account_yes = "delete_account_yes"
    const val delete_account_no = "delete_account_no"
    const val premium_continue = "premium_continue"
    const val dip_start = "dip_start"
    const val dip_sent = "dip_sent"
    const val block_user_start = "block_user_start"
    const val block_user_yes = "block_user_yes"
    const val block_user_no = "block_user_no"
    const val report_user = "report_user"
    const val social_id = "social_id"
    const val user_token = "user_token"
    const val is_social = "is_social"
    const val login_type = "login_type"
    var needToWait = false
}