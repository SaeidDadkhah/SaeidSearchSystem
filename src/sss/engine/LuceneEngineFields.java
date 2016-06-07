package sss.engine;

import sss.LuceneEngine;

/**
 * Created by Saeid Dadkhah on 2016-03-13 2:41 PM.
 * Project: SSS
 */
public class LuceneEngineFields {

    public static final int F_NAME_PATH = 0;
    public static final int F_NAME_FROM = 1;
    public static final int F_NAME_NEWSGROUP = 2;
    public static final int F_NAME_SUBJECT = 3;
    public static final int F_NAME_MESSAGE_ID = 4;
    public static final int F_NAME_X_XXMESSAGE_ID = 5;
    public static final int F_NAME_DATE = 6;
    public static final int F_NAME_X_XXDATE = 7;
    public static final int F_NAME_ARTICLE_ID = 8;
    public static final int F_NAME_ORGANIZATION = 9;
    public static final int F_NAME_LINES = 10;
    public static final int F_NAME_NNTP_POSTING_HOST = 11;
    public static final int F_NAME_X_NEWREADER = 12;
    public static final int F_NAME_XREF = 13;
    public static final int F_NAME_EXPIRES = 14;
    public static final int F_NAME_DISTRIBUTION = 15;
    public static final int F_NAME_KEYWORDS = 16;
    public static final int F_NAME_REFERENCES = 17;
    public static final int F_NAME_SENDER = 18;
    public static final int F_NAME_REPLY_TO = 19;
    public static final int F_NAME_IN_REPLY_TO = 21;
    public static final int F_NAME_FOLLOW_UP = 22;
    public static final int F_NAME_FOLLOW_UPS = 23;
    public static final int F_NAME_FOLLOWUP_TO = 24;
    public static final int F_NAME_X_FOLLOWUP_TO = 25;
    public static final int F_NAME_X_READER = 26;
    public static final int F_NAME_X_MAILER = 27;
    public static final int F_NAME_SUMMARY = 28;
    public static final int F_NAME_X_DISCLAIMER = 29;
    public static final int F_NAME_X_STANDARD_DISCLAIMER = 30;
    public static final int F_NAME_ORIGINATOR = 31;
    public static final int F_NAME_X_USERAGENT = 32;
    public static final int F_NAME_X_FTN_TO = 33;
    public static final int F_NAME_X_AUTH_USER = 34;
    public static final int F_NAME_X_POSTED_FROM = 35;
    public static final int F_NAME_NEWS_SOFTWARE = 36;
    public static final int F_NAME_X_NEWS_SOFTWARE = 37;
    public static final int F_NAME_X_GATED_BY = 38;
    public static final int F_NAME_X_STATUS = 39;
    public static final int F_NAME_TEL = 40;
    public static final int F_NAME_APPROVED = 41;
    public static final int F_NAME_X_RECEIVED = 42;
    public static final int F_NAME_NNTP_SOFTWARE = 43;
    public static final int F_NAME_POSTING_FRONT_END = 44;
    public static final int F_NAME_X_SUBLIMINAL_MESSAGE = 45;
    public static final int F_NAME_MIME_VERSION = 46;
    public static final int F_NAME_CONTENT_TYPE = 47;
    public static final int F_NAME_CONTENT_TRANSFER_ENCODING = 48;
    public static final int F_NAME_X_TO = 49;
    public static final int F_NAME_X_NEWS_READER = 50;
    public static final int F_NAME_FILE_ADDRESS = 51;
    public static final int F_NAME_BODY = 52;

    public static final int NUM_OF_FIELDS = 53;

    // Returns -1 if name is not valid
    public static int getId(String name) {
        switch (name) {
            case "path":
                return F_NAME_PATH;
            case "from":
                return F_NAME_FROM;
            case "newsgroups":
                return F_NAME_NEWSGROUP;
            case "subject":
                return F_NAME_SUBJECT;
            case "message-id":
                return F_NAME_MESSAGE_ID;
            case "x-xxmessage-id":
                return F_NAME_X_XXMESSAGE_ID;
            case "date":
                return F_NAME_DATE;
            case "x-xxdate":
                return F_NAME_X_XXDATE;
            case "article-i.d.":
                return F_NAME_ARTICLE_ID;
            case "organization":
                return F_NAME_ORGANIZATION;
            case "lines":
                return F_NAME_LINES;
            case "nntp-posting-host":
                return F_NAME_NNTP_POSTING_HOST;
            case "x-newsreader": // MAYBE X_NEWS_READER is required!
                return F_NAME_X_NEWREADER;
            case "xref":
                return F_NAME_XREF;
            case "expires":
                return F_NAME_EXPIRES;
            case "distribution":
                return F_NAME_DISTRIBUTION;
            case "keywords":
                return F_NAME_KEYWORDS;
            case "references":
                return F_NAME_REFERENCES;
            case "sender":
                return F_NAME_SENDER;
            case "reply-to":
                return F_NAME_REPLY_TO;
            case "in-reply-to":
                return F_NAME_IN_REPLY_TO;
            case "follow-up":
                return F_NAME_FOLLOW_UP;
            case "follow-ups":
                return F_NAME_FOLLOW_UPS;
            case "followup-to":
                return F_NAME_FOLLOWUP_TO;
            case "x-followup-to":
                return F_NAME_X_FOLLOWUP_TO;
            case "x-reader":
                return F_NAME_X_READER;
            case "x-mailer":
                return F_NAME_X_MAILER;
            case "summary":
                return F_NAME_SUMMARY;
            case "x-disclaimer":
                return F_NAME_X_DISCLAIMER;
            case "x-standard-disclaimer":
                return F_NAME_X_STANDARD_DISCLAIMER;
            case "originator":
                return F_NAME_ORIGINATOR;
            case "x-useragent":
                return F_NAME_X_USERAGENT;
            case "x-ftn-to":
                return F_NAME_X_FTN_TO;
            case "x-auth-user":
                return F_NAME_X_AUTH_USER;
            case "x-posted-from":
                return F_NAME_X_POSTED_FROM;
            case "news-software":
                return F_NAME_NEWS_SOFTWARE;
            case "x-news-software":
                return F_NAME_X_NEWS_SOFTWARE;
            case "x-gated-by":
                return F_NAME_X_GATED_BY;
            case "x-status":
                return F_NAME_X_STATUS;
            case "tel":
                return F_NAME_TEL;
            case "approved":
                return F_NAME_APPROVED;
            case "x-received":
                return F_NAME_X_RECEIVED;
            case "nntp-software":
                return F_NAME_NNTP_SOFTWARE;
            case "posting-front-end":
                return F_NAME_POSTING_FRONT_END;
            case "x-subliminal-message":
                return F_NAME_X_SUBLIMINAL_MESSAGE;
            case "mime-version":
                return F_NAME_MIME_VERSION;
            case "content-type":
                return F_NAME_CONTENT_TYPE;
            case "content-transfer-encoding":
                return F_NAME_CONTENT_TRANSFER_ENCODING;
            case "x-to":
                return F_NAME_X_TO;
            case "x-news-reader":
                return F_NAME_X_NEWS_READER;
            case "file-address":
                return F_NAME_FILE_ADDRESS;
            case "body":
                return F_NAME_BODY;
            default:
                return -1;
        }
    }

    // Returns null if id is not valid.
    public static String getName(int id) {
        switch (id) {
            case F_NAME_PATH:
                return "path";
            case F_NAME_FROM:
                return "from";
            case F_NAME_NEWSGROUP:
                return "newsgroups";
            case F_NAME_SUBJECT:
                return "subject";
            case F_NAME_MESSAGE_ID:
                return "message-id";
            case F_NAME_X_XXMESSAGE_ID:
                return "x-xxmessage-id";
            case F_NAME_DATE:
                return "date";
            case F_NAME_X_XXDATE:
                return "x-xxdate";
            case F_NAME_ARTICLE_ID:
                return "article-i.d.";
            case F_NAME_ORGANIZATION:
                return "organization";
            case F_NAME_LINES:
                return "lines";
            case F_NAME_NNTP_POSTING_HOST:
                return "nntp-posting-host";
            case F_NAME_X_NEWREADER: // MAYBE X_NEWS_READER is required!
                return "x-newsreader";
            case F_NAME_XREF:
                return "xref";
            case F_NAME_EXPIRES:
                return "expires";
            case F_NAME_DISTRIBUTION:
                return "distribution";
            case F_NAME_KEYWORDS:
                return "keywords";
            case F_NAME_REFERENCES:
                return "references";
            case F_NAME_SENDER:
                return "sender";
            case F_NAME_REPLY_TO:
                return "reply-to";
            case F_NAME_IN_REPLY_TO:
                return "in-reply-to";
            case F_NAME_FOLLOW_UP:
                return "follow-up";
            case F_NAME_FOLLOW_UPS:
                return "follow-ups";
            case F_NAME_FOLLOWUP_TO:
                return "followup-to";
            case F_NAME_X_FOLLOWUP_TO:
                return "x-followup-to";
            case F_NAME_X_READER:
                return "x-reader";
            case F_NAME_X_MAILER:
                return "x-mailer";
            case F_NAME_SUMMARY:
                return "summary";
            case F_NAME_X_DISCLAIMER:
                return "x-disclaimer";
            case F_NAME_X_STANDARD_DISCLAIMER:
                return "x-standard-disclaimer";
            case F_NAME_ORIGINATOR:
                return "originator";
            case F_NAME_X_USERAGENT:
                return "x-useragent";
            case F_NAME_X_FTN_TO:
                return "x-ftn-to";
            case F_NAME_X_AUTH_USER:
                return "x-auth-user";
            case F_NAME_X_POSTED_FROM:
                return "x-posted-from";
            case F_NAME_NEWS_SOFTWARE:
                return "news-software";
            case F_NAME_X_NEWS_SOFTWARE:
                return "x-news-software";
            case F_NAME_X_GATED_BY:
                return "x-gated-by";
            case F_NAME_X_STATUS:
                return "x-status";
            case F_NAME_TEL:
                return "tel";
            case F_NAME_APPROVED:
                return "approved";
            case F_NAME_X_RECEIVED:
                return "x-received";
            case F_NAME_NNTP_SOFTWARE:
                return "nntp-software";
            case F_NAME_POSTING_FRONT_END:
                return "posting-front-end";
            case F_NAME_X_SUBLIMINAL_MESSAGE:
                return "x-subliminal-message";
            case F_NAME_MIME_VERSION:
                return "mime-version";
            case F_NAME_CONTENT_TYPE:
                return "content-type";
            case F_NAME_CONTENT_TRANSFER_ENCODING:
                return "content-transfer-encoding";
            case F_NAME_X_TO:
                return "x-to";
            case F_NAME_X_NEWS_READER:
                return "x-news-reader";
            case F_NAME_FILE_ADDRESS:
                return "file-address";
            case F_NAME_BODY:
                return "body";
            default:
                return null;
            }
        }


    public static int getType(int id) {
        switch (id) {
            case F_NAME_PATH:
                return LuceneEngine.F_TYPE_NOT_TOKENIZE;
            case F_NAME_FROM:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_NEWSGROUP:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_SUBJECT:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_MESSAGE_ID:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_XXMESSAGE_ID:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_DATE:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_XXDATE:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_ARTICLE_ID:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_ORGANIZATION:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_LINES:
                return LuceneEngine.F_TYPE_NOT_TOKENIZE;
            case F_NAME_NNTP_POSTING_HOST:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_NEWREADER:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_XREF:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_EXPIRES:
                return LuceneEngine.F_TYPE_NOT_TOKENIZE;
            case F_NAME_DISTRIBUTION:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_KEYWORDS:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_REFERENCES:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_SENDER:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_REPLY_TO:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_IN_REPLY_TO:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_FOLLOW_UP:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_FOLLOW_UPS:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_FOLLOWUP_TO:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_FOLLOWUP_TO:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_READER:
                return LuceneEngine.F_TYPE_NOT_TOKENIZE;
            case F_NAME_X_MAILER:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_SUMMARY:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_DISCLAIMER:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_STANDARD_DISCLAIMER:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_ORIGINATOR:
                return LuceneEngine.F_TYPE_NOT_TOKENIZE;
            case F_NAME_X_USERAGENT:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_FTN_TO:
                return LuceneEngine.F_TYPE_NOT_TOKENIZE;
            case F_NAME_X_AUTH_USER:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_POSTED_FROM:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_NEWS_SOFTWARE:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_NEWS_SOFTWARE:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_GATED_BY:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_STATUS:
                return LuceneEngine.F_TYPE_NOT_TOKENIZE;
            case F_NAME_TEL:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_APPROVED:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_RECEIVED:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_NNTP_SOFTWARE:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_POSTING_FRONT_END:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_SUBLIMINAL_MESSAGE:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_MIME_VERSION:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_CONTENT_TYPE:
                return LuceneEngine.F_TYPE_NOT_TOKENIZE;
            case F_NAME_CONTENT_TRANSFER_ENCODING:
                return LuceneEngine.F_TYPE_NOT_TOKENIZE;
            case F_NAME_X_TO:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_X_NEWS_READER:
                return LuceneEngine.F_TYPE_TOKENIZE;
            case F_NAME_FILE_ADDRESS:
                return LuceneEngine.F_TYPE_NOT_TOKENIZE;
            case F_NAME_BODY:
                return LuceneEngine.F_TYPE_TOKENIZE;
            default:
                return LuceneEngine.F_TYPE_UNKNOWN;
        }
    }

}