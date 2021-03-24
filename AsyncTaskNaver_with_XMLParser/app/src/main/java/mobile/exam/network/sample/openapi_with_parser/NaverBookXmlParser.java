package mobile.exam.network.sample.openapi_with_parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;


public class NaverBookXmlParser {
    //    xml에서 읽어들일 태그를 구분한 enum  → 정수값 등으로 구분하지 않고 가독성 높은 방식을 사용
    private enum TagType { NONE, BOOK_TITLE, IMAGE, AUTHOR, PRICE };

    //    parsing 대상인 tag를 상수로 선언
    private final static String FAULT_RESULT = "faultResult";
    private final static String ITEM_TAG = "item";
    private final static String BOOK_TITLE_TAG = "title";
    private final static String IMAGE_TAG = "image";
    private final static String AUTHOR_TAG = "author";
    private final static String PRICE_TAG = "price";

    private XmlPullParser parser;

    public NaverBookXmlParser() {
        try {
            parser = XmlPullParserFactory.newInstance().newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<NaverBookDto> parse(String xml) {
        ArrayList<NaverBookDto> resultList = new ArrayList();
        NaverBookDto dbo = null;
        TagType tagType = TagType.NONE;     //  태그를 구분하기 위한 enum 변수 초기화

        try {
            // 파싱 대상 지정
            parser.setInput(new StringReader(xml));

//          for 반복문 사용 경우
//          for (int eventType = parser.getEventType();
//          eventType!= XmlPullParser.END_DOCUMENT;
//          eventType = parser.next())

            // 태그 유형 구분 변수 준비
            int eventType = parser.getEventType();

            // parsing 수행 - for 문 또는 while 문으로 구성
            while (eventType!=XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
//          item의 시작태그를 읽는 순간 dto 준비
                        if (tag.equals(ITEM_TAG)){
                            dbo = new NaverBookDto();
                        } else if (tag.equals(BOOK_TITLE_TAG)){
//          <item>안의 <title>태그가 아닌 아이템 밖의 <title>태그를 읽는 것을 방지하기 위해 dbo가 존재하는지 확인함
                            if (dbo!=null) tagType = TagType.BOOK_TITLE;
                        } else if (tag.equals(IMAGE_TAG)){
                            tagType = TagType.IMAGE;
                        } else if (tag.equals(AUTHOR_TAG)){
                            tagType=TagType.AUTHOR;
                        } else if (tag.equals(PRICE_TAG)){
                            tagType=TagType.PRICE;
                        } else if (tag.equals(FAULT_RESULT)) {
                            return null;
                        }
                        break;
                    case XmlPullParser.END_TAG:
//                        아이템의 마지막을 나타내므로 이 태그를 만나면 list에 dto 저장
                        if (parser.getName().equals(ITEM_TAG)){
                            resultList.add(dbo);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType){
                            case BOOK_TITLE:
//                                getText() : TEXT값 얻어오기
                                dbo.setTitle(parser.getText());
                                break;
                            case IMAGE:
                                dbo.setImageLink(parser.getText());
                                break;
                            case AUTHOR:
                                dbo.setAuthor(parser.getText());
                                break;
                            case PRICE:
                                dbo.setPrice(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}