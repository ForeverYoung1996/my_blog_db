package com.site.blog.my.core.controller.blog;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.site.blog.my.core.controller.vo.BlogDetailVO;
import com.site.blog.my.core.entity.BlogComment;
import com.site.blog.my.core.entity.BlogLink;
import com.site.blog.my.core.service.*;
import com.site.blog.my.core.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link http://13blog.site
 */
@Controller
public class MyBlogController {

    private static final Logger logger = LoggerFactory.getLogger(MyBlogController.class);
    private static final String BAIDU_APP_KEY = "uVEpXzOtx0t41c2pKSsXh2VWr0DbiroK";

    //public static String theme = "default";
    public static String theme = "yummy-jekyll";
//    public static String theme = "amaze";
    @Resource
    private BlogService blogService;
    @Resource
    private TagService tagService;
    @Resource
    private LinkService linkService;
    @Resource
    private CommentService commentService;
    @Resource
    private ConfigService configService;
    @Resource
    private CategoryService categoryService;

    @Autowired
    private MailService mailService;

    /**
     * 定位
     */
    @RequestMapping("/position")
    @ResponseBody
    public Result position(String local,String ip) {


        String to = "1021558365@qq.com";
        String subject = "网站访问提醒!";
        String content = "详细地址:" + local +"  !  "+ ip;

        new Thread(new Runnable() {
            @Override
            public void run() {
                mailService.sendSimpleMail(to,subject,content);
                logger.info("邮件发送中...");
            }
        }).start();


        return ResultGenerator.genSuccessResult(local + ip);
    }


    /**
     * 首页
     *
     * @return
     */
    @GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request) {
        return this.page(request, 1);
    }

    /**
     * 首页 分页数据
     *
     * @return
     */
    @GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum) {
        PageResult blogPageResult = blogService.getBlogsForIndexPage(pageNum);
        if (blogPageResult == null) {
            return "error/error_404";
        }
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("pageName", "首页");
        request.setAttribute("configurations", configService.getAllConfigs());

        String header = request.getHeader("User-Agent");
        String ip = IPKit.getIpAddrByRequest(request);
        logger.info("访问设备:"+header +" >>> 访问地址:"+ip );
        String location = getIpLocation(ip);
        String date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date());
        String content =date + " 主机ip为 "+ip +",ip地理位置: "+location+",正在使用 "+header+" 设备访问了你的网站主页.";

//        if (!"0:0:0:0:0:0:0:1".startsWith(ip)){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    mailService.sendSimpleMail(to,subject,content);
//                    logger.info("邮件发送中...");
//                }
//            }).start();
//
//        }

        request.setAttribute("ip", content);
        return "blog/" + theme + "/index";
    }

    public static String getIpLocation(String ip){
        String result = "未知地址";
        if (!StringUtils.isEmpty(ip)){
            String url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query="+ip+"&co=&resource_id=6006&t=1563871835695&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery11020995488703613727_1563870746913&_=1563870747047";
            String s = null;
            try {
                s = HttpUtils.get(url);
                logger.info("返回json:" + s);
                String ret = s.substring(s.indexOf("{"),s.length()-2);
                logger.info("截取后的json:" + ret);
                JSONObject jsonObject = JSONObject.parseObject(ret);
                Object status = jsonObject.get("status");
                if (status != null && "0".equals(status.toString())){
                    Object data = jsonObject.get("data");
                    if (data != null && data instanceof JSONArray){
                        JSONArray jsonArray = (JSONArray) data;
                        if (jsonArray != null && !jsonArray.isEmpty()){
                            Object o = jsonArray.get(0);
                            JSONObject json = (JSONObject) o;
                            Object location = json.get("location");
                            result = location.toString();
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;

    }



    public static void main(String[] args) {
//        String ipLocation = getIpLocation("122.96.43.233");
//        System.out.println(ipLocation);
        
        String ip = "122.96.43.233";
        String url = "http://api.map.baidu.com/location/ip?ak=" + BAIDU_APP_KEY+"&ip="+ip;

        String s = null;
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0 ; i < 100 ; i ++){
                        try {
                            HttpUtils.get("https://www.yilukkk.com/index");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0 ; i < 100 ; i ++){
                        try {
                            HttpUtils.get("https://www.yilukkk.com/index");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0 ; i < 100 ; i ++){
                        try {
                            HttpUtils.get("https://www.yilukkk.com/index");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            System.out.println("=========请求结束========");
//            s = URLDecoder.decode(s,"utf-8");
//            JSONObject jsonObject = JSONObject.parseObject(s);
//            String s2 = jsonObject.get("address").toString();
//            System.out.println(URLDecoder.decode(s2,"utf-8"));
//            System.out.println(s);
//            String url1 = "http://api.map.baidu.com/geocoder/v2/?output=json&ak="+BAIDU_APP_KEY+"&location=" +"3747938.29,13222458.63" + "&output=json";

//        String url1 = "http://api.map.baidu.com/geocoder/v2/?ak="+BAIDU_APP_KEY+"&callback=renderReverse&location="+"3747938.29,13222458.63"+"&output=json&pois=1";
//            String s1 = HttpUtils.post(url1,null);
//            System.out.println(s1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Categories页面(包括分类数据和标签数据)
     *
     * @return
     */
    @GetMapping({"/categories"})
    public String categories(HttpServletRequest request) {
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("categories", categoryService.getAllCategories());
        request.setAttribute("pageName", "分类页面");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/category";
    }

    /**
     * 详情页
     *
     * @return
     */
    @GetMapping({"/blog/{blogId}", "/article/{blogId}"})
    public String detail(HttpServletRequest request, @PathVariable("blogId") Long blogId, @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        BlogDetailVO blogDetailVO = blogService.getBlogDetail(blogId);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
            request.setAttribute("commentPageResult", commentService.getCommentPageByBlogIdAndPageNum(blogId, commentPage));
        }
        request.setAttribute("pageName", "详情");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/detail";
    }

    /**
     * 标签列表页
     *
     * @return
     */
    @GetMapping({"/tag/{tagName}"})
    public String tag(HttpServletRequest request, @PathVariable("tagName") String tagName) {
        return tag(request, tagName, 1);
    }

    /**
     * 标签列表页
     *
     * @return
     */
    @GetMapping({"/tag/{tagName}/{page}"})
    public String tag(HttpServletRequest request, @PathVariable("tagName") String tagName, @PathVariable("page") Integer page) {
        PageResult blogPageResult = blogService.getBlogsPageByTag(tagName, page);
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "标签");
        request.setAttribute("pageUrl", "tag");
        request.setAttribute("keyword", tagName);
        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/list";
    }

    /**
     * 分类列表页
     *
     * @return
     */
    @GetMapping({"/category/{categoryName}"})
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName) {
        return category(request, categoryName, 1);
    }

    /**
     * 分类列表页
     *
     * @return
     */
    @GetMapping({"/category/{categoryName}/{page}"})
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName, @PathVariable("page") Integer page) {
        PageResult blogPageResult = blogService.getBlogsPageByCategory(categoryName, page);
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "分类");
        request.setAttribute("pageUrl", "category");
        request.setAttribute("keyword", categoryName);
        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/list";
    }

    /**
     * 搜索列表页
     *
     * @return
     */
    @GetMapping({"/search/{keyword}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword) {
        return search(request, keyword, 1);
    }

    /**
     * 搜索列表页
     *
     * @return
     */
    @GetMapping({"/search/{keyword}/{page}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("page") Integer page) {
        PageResult blogPageResult = blogService.getBlogsPageBySearch(keyword, page);
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "搜索");
        request.setAttribute("pageUrl", "search");
        request.setAttribute("keyword", keyword);
        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/list";
    }


    /**
     * 友情链接页
     *
     * @return
     */
    @GetMapping({"/link"})
    public String link(HttpServletRequest request) {
        request.setAttribute("pageName", "友情链接");
        Map<Byte, List<BlogLink>> linkMap = linkService.getLinksForLinkPage();
        if (linkMap != null) {
            //判断友链类别并封装数据 0-友链 1-推荐 2-个人网站
            if (linkMap.containsKey((byte) 0)) {
                request.setAttribute("favoriteLinks", linkMap.get((byte) 0));
            }
            if (linkMap.containsKey((byte) 1)) {
                request.setAttribute("recommendLinks", linkMap.get((byte) 1));
            }
            if (linkMap.containsKey((byte) 2)) {
                request.setAttribute("personalLinks", linkMap.get((byte) 2));
            }
        }
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/link";
    }

    /**
     * 评论操作
     */
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public Result comment(HttpServletRequest request, HttpSession session,
                          @RequestParam Long blogId, @RequestParam String verifyCode,
                          @RequestParam String commentator, @RequestParam String email,
                          @RequestParam String websiteUrl, @RequestParam String commentBody) {
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (!verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult("验证码错误");
        }
        String ref = request.getHeader("Referer");
        if (StringUtils.isEmpty(ref)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (null == blogId || blogId < 0) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (StringUtils.isEmpty(commentator)) {
            return ResultGenerator.genFailResult("请输入称呼");
        }
        if (StringUtils.isEmpty(email)) {
            return ResultGenerator.genFailResult("请输入邮箱地址");
        }
        if (!PatternUtil.isEmail(email)) {
            return ResultGenerator.genFailResult("请输入正确的邮箱地址");
        }
        if (StringUtils.isEmpty(commentBody)) {
            return ResultGenerator.genFailResult("请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }
        BlogComment comment = new BlogComment();
        comment.setBlogId(blogId);
        comment.setCommentator(MyBlogUtils.cleanString(commentator));
        comment.setEmail(email);
        if (PatternUtil.isURL(websiteUrl)) {
            comment.setWebsiteUrl(websiteUrl);
        }
        comment.setCommentBody(MyBlogUtils.cleanString(commentBody));
        return ResultGenerator.genSuccessResult(commentService.addComment(comment));
    }

    /**
     * 关于页面 以及其他配置了subUrl的文章页
     *
     * @return
     */
    @GetMapping({"/{subUrl}"})
    public String detail(HttpServletRequest request, @PathVariable("subUrl") String subUrl) {
        BlogDetailVO blogDetailVO = blogService.getBlogDetailBySubUrl(subUrl);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
            request.setAttribute("pageName", subUrl);
            request.setAttribute("configurations", configService.getAllConfigs());
            return "blog/" + theme + "/detail";
        } else {
            return "error/error_400";
        }
    }
}
