package edu.iscas.appcheckclient.util;

public class Test趣头条{
public void test(){
onView(withId("com.jifen.qukan:id/edt_login_phone")).perform(typeText("1"));
onView(withId("com.jifen.qukan:id/edt_login_phone")).perform(typeText("18"));
onView(withId("com.jifen.qukan:id/edt_login_phone")).perform(typeText("184"));
onView(withId("com.jifen.qukan:id/edt_login_phone")).perform(typeText("1840"));
onView(withId("com.jifen.qukan:id/edt_login_phone")).perform(typeText("18401"));
onView(withId("com.jifen.qukan:id/edt_login_phone")).perform(typeText("184016"));
onView(withId("com.jifen.qukan:id/edt_login_phone")).perform(typeText("1840169"));
onView(withId("com.jifen.qukan:id/edt_login_phone")).perform(typeText("18401698"));
onView(withId("com.jifen.qukan:id/edt_login_phone")).perform(typeText("184016980"));
onView(withId("com.jifen.qukan:id/edt_login_phone")).perform(typeText("1840169809"));
onView(withId("com.jifen.qukan:id/edt_login_phone")).perform(typeText("18401698098"));
onView(withId("com.jifen.qukan:id/edt_login_pwd")).perform(typeText(password));
onView(withId("com.jifen.qukan:id/edt_login_pwd")).perform(typeText(password));
onView(withId("com.jifen.qukan:id/edt_login_pwd")).perform(typeText(password));
onView(withId("com.jifen.qukan:id/edt_login_pwd")).perform(typeText(password));
onView(withId("com.jifen.qukan:id/edt_login_pwd")).perform(typeText(password));
onView(withId("com.jifen.qukan:id/edt_login_pwd")).perform(typeText(password));
onView(withXPath("/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout[2]/android.widget.RelativeLayout/android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.View/android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[1]")).perform(click());
onView(withId("com.jifen.qukan:id/and_edt_comment")).perform(typeText("q"));
onView(withId("com.jifen.qukan:id/and_edt_comment")).perform(typeText("qwe"));
onView(withId("com.jifen.qukan:id/and_edt_comment")).perform(typeText("qwe"));
onView(withId("com.jifen.qukan:id/and_btn_send")).perform(click());
Espresso.pressBack();
onView(withXPath("/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout[2]/android.widget.FrameLayout/android.view.View/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.FrameLayout[4]")).perform(click());
}
}
