package app.avos;

import android.content.Context;
import android.widget.Toast;
import com.avos.avoscloud.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lewiskit on 14-10-17.
 */
public class WrapData {

    /**
     * @return    the return type can get file and article title
     *            by using avArticle.getString("title")
     *            the file can see the following explaination in the method
     * get the special date article from server
     * todo get the article file from server
     * now we define the article is file on the server
     * we identify the article with date using the YYYY-MM-DD
     * the method must run in a back process
     */
    public static AVObject getAVArticle(String date) {

        //article

        AVQuery<AVObject> query = new AVQuery<AVObject>("article");

        //set query filter
        query.whereEqualTo("date", date);

        try {
            List<AVObject> avArticles = query.find();

            if (avArticles.size() > 0) {
                AVObject avArticle = avArticles.get(0);

                /** the next thing is to
                 //get content and title
                 AVFile avArticleContent = avObject.getAVFile("content");
                 String avArticleTitle = avObject.getString("title");

                 avArticleContent.getDataInBackground(new GetDataCallback() {
                @Override public void done(byte[] bytes, AVException e) {
                converBytesToFile(bytes);
                }
                });
                 */

                return avArticle;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    /**
     * @param avArticle
     * @return todo get the article comments from the server
     * the return List 's size cloud be 0
     */
    public static List<String> getArticleCommentsList(AVObject avArticle) {

        AVQuery<AVObject> query = AVQuery.getQuery("articleComment");
        query.whereEqualTo("article", avArticle);

        List<String> articleCommentList = new ArrayList<String>();

        try {
            List<AVObject> avArticleCommentList = query.find();

            for (AVObject AVarticleComment : avArticleCommentList) {

                String comment = AVarticleComment.getString("content");

                articleCommentList.add(comment);

            }


        } catch (Exception e) {

            e.printStackTrace();
        }


        return articleCommentList;

    }

    /**
     * @param avArticle
     * @param context
     * @param content   submit a comment for article
     */
    public static void submitArtcleComment(AVObject avArticle, final Context context, String content) {

        AVObject articleComment = new AVObject("articleComment");

        //set the value to the object
        articleComment.put("content", content);
        articleComment.put("article", avArticle);

        //save and start a background process

        articleComment.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {

                String text = "";
                if (e == null) {
                    text = "保存成功";
                } else {
                    text = "保存失败";
                }

                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });


    }


    /**
     * @param date     the date format should be YY-MM-DD
     * @return         the return type is AVObject and we can
     *                 use avTopic.getString("content") to get the topic
     * todo get the current topic from the server
     * the method must run in a back process
     */
    public static AVObject getAVOSTopic(String date) {

        AVQuery<AVObject> query = AVQuery.getQuery("topic");
        //set the query filter
        query.whereEqualTo("date", date);

        try {
            List<AVObject> avTopicList = query.find();

            if (avTopicList.size() > 0) {

                AVObject avTopic = avTopicList.get(0);
                /**
                String topic = avTopic.getString("content");
                **/
                return avTopic;

            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return null;
    }

    /**
     * @param avTopic
     * @return todo: get the topic comments from the server
     * the method must run in a process
     */
    public List<String> getTopicComment(AVObject avTopic) {

        //the list of comments
        List<String> topicCommentList = new ArrayList<String>();

        AVQuery<AVObject> query = AVQuery.getQuery("topicComment");
        //set the query filter
        query.whereEqualTo("topic", avTopic);

        try {
            //query
            List<AVObject> avTopicCommentList = query.find();

            //get the value of comment
            for (AVObject avTopicComment : avTopicCommentList) {

                String topicComment = avTopicComment.getString("content");
                topicCommentList.add(topicComment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return topicCommentList;
    }


    /**
     *
     * @param AVTopic
     * @param content
     * @param context
     */
    public static void submitTopicComment(AVObject AVTopic, String content, final Context context){
        AVObject avTopicComment = new AVObject("topicComment");

        //set the values to the keys
        avTopicComment.put("content",content);
        avTopicComment.put("topic",AVTopic);

        avTopicComment.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {

                String text = "";
                if (e == null){
                    text = "保存成功";
                }else{
                    text = "保存失败";
                }

                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
