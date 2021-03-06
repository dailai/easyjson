package com.jn.easyjson.tests.cases;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;
import com.jn.easyjson.core.JSON;
import com.jn.easyjson.core.JSONBuilder;
import com.jn.easyjson.core.JSONBuilderProvider;
import com.jn.easyjson.tests.entity.struct.Contact;
import com.jn.easyjson.tests.entity.struct.PagingRequest;
import com.jn.easyjson.tests.entity.struct.PagingRequestContext;
import com.jn.easyjson.tests.entity.struct.PagingResult;
import com.jn.easyjson.tests.entity.struct.Person;
import com.jn.easyjson.tests.entity.user.Gender;
import com.jn.easyjson.tests.utils.Asserts;
import com.jn.langx.util.reflect.type.Types;

/**
 * PagingRequest复杂对象测试
 *
 * @author zhaohuihua
 * @version 20200615
 */
public abstract class EasyJsonPagingRequestTest extends AbstractBaseTest {

    private JSON json;
    private Person person;
    private List<Person> persons = new ArrayList<Person>();
    private Map<String, Person> nameToPersonMap = new HashMap<String, Person>();
    private Map<Integer, Person> idToPersonMap = new HashMap<Integer, Person>();
    private PagingRequest<Map<String, Object>, Person> pagingRequest;

    public EasyJsonPagingRequestTest() {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        jsonBuilder.serializeNulls(true).serializeNumberAsString(false);
        // IgnoreAnnotationExclusion不在这里测试, 单独写用例
        // jsonBuilder.addDeserializationExclusion(new IgnoreAnnotationExclusion());
        this.json = jsonBuilder.build();

        for (int i = 1; i <= 10; i++) {
            Person p = new Person();
            p.setId(i);
            p.setName("name_" + i);
            p.setBirthday(new Date(1592179316459L));
            p.setGender(i % 2 == 0 ? Gender.MALE : Gender.FEMALE);
            p.setAuthCode(12312425353464564L + i);

            Contact c = new Contact();
            p.setContact(c);
            c.setEmail(p.getName() + "@gmail.com");
            c.setMobilePhone("mphone" + i);
            c.setPhone("phone" + i);
            c.setMsn("msn" + i);
            c.setWebchat("webchat" + i);

            if (i == 1) {
                person = p;
            }

            persons.add(person);
        }

        PagingRequest<Map<String, Object>, Person> pagingRequest = new PagingRequest<>();
        pagingRequest.setCacheCount(true);
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("a", 1);
        condition.put("b", 2);
        pagingRequest.setCondition(condition);
        pagingRequest.setCountColumn("name_");
        pagingRequest.setPageNo(20);
        pagingRequest.setPageSize(50);
        PagingRequestContext<Map<String, Object>, Person> pagingRequestContext = new PagingRequestContext<>();
        pagingRequest.setCtx(pagingRequestContext);
        PagingResult<Person> result = new PagingResult<>();
        result.setItems(persons);
        result.setTotal(203423);
        result.setPageNo(pagingRequest.getPageNo());
        result.setPageSize(pagingRequest.getPageSize());
        pagingRequest.setResult(result);
        this.pagingRequest = pagingRequest;
    }

    protected Person getPersonObject() {
        return person;
    }

    protected String getPersonString() {
        return readClassResourceText(EasyJsonPagingRequestTest.class, "json/PersonObjectString.json");
    }

    protected List<Person> getPersonsObject() {
        return persons;
    }

    protected String getPersonsString() {
        return readClassResourceText(EasyJsonPagingRequestTest.class, "json/PersonArrayString.json");
    }

    protected Person getPersonById(int id) {
        return idToPersonMap.get(id);
    }

    protected Person getPersonByName(String name) {
        return nameToPersonMap.get(name);
    }

    protected PagingRequest<Map<String, Object>, Person> getPagingRequestObject() {
        return pagingRequest;
    }

    protected String getPagingRequestStringIncludeNulls() {
        return readClassResourceText(EasyJsonPagingRequestTest.class, "json/PagingRequestIncludeNulls.json");
    }

    protected String getPagingRequestStringExcludeNulls() {
        return readClassResourceText(EasyJsonPagingRequestTest.class, "json/PagingRequestExcludeNulls.json");
    }

    protected String getPagingRequestStringNumberAsString() {
        return readClassResourceText(EasyJsonPagingRequestTest.class, "json/PagingRequestNumberAsString.json");
    }

    @Test(priority = 10001)
    public void testPagingRequestSerializeIncludeNulls10001() {
        PagingRequest<Map<String, Object>, Person> object = getPagingRequestObject();
        String jsonString = json.toJson(object);
        System.out.println(jsonString);
        Asserts.assertJsonEquals(jsonString, getPagingRequestStringIncludeNulls());
    }

    @Test(priority = 10002)
    public void testPagingRequestSerializeExcludeNulls10002() {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        jsonBuilder.serializeNulls(false).serializeNumberAsString(false);
        JSON json = jsonBuilder.build();
        PagingRequest<Map<String, Object>, Person> object = getPagingRequestObject();
        String jsonString = json.toJson(object);
        System.out.println(jsonString);
        Asserts.assertJsonEquals(jsonString, getPagingRequestStringExcludeNulls());
    }

    @Test(priority = 10003)
    public void testPagingRequestSerializeNumberAsString10003() {
        JSONBuilder jsonBuilder = JSONBuilderProvider.create();
        jsonBuilder.serializeNulls(true).serializeNumberAsString(true);
        JSON json = jsonBuilder.build();
        PagingRequest<Map<String, Object>, Person> object = getPagingRequestObject();
        String jsonString = json.toJson(object);
        System.out.println(jsonString);
        Asserts.assertJsonEquals(jsonString, getPagingRequestStringNumberAsString());
    }

    @Test(priority = 10101)
    public void testPersonSerialize10101() {
        Person object = getPersonObject();
        String jsonString = json.toJson(object);
        System.out.println(jsonString);
        Asserts.assertJsonEquals(jsonString, getPersonString());
    }

    @Test(priority = 10102)
    public void testPersonDeserialize10102() {
        String jsonString = getPersonString();
        Person actual = json.fromJson(jsonString, Person.class);
        Person expected = getPersonObject();
        Asserts.assertDeepEquals(actual, expected);
    }

    @Test(priority = 10201)
    public void testPersonsSerialize10201() {
        List<Person> object = getPersonsObject();
        String jsonString = json.toJson(object);
        System.out.println(jsonString);
        Asserts.assertJsonEquals(jsonString, getPersonsString());
    }

    @Test(priority = 10202)
    public void testPersonsDeserialize10202() {
        String jsonString = getPersonsString();
        List<Person> actual = json.fromJson(jsonString, Types.getListParameterizedType(Person.class));
        List<Person> expected = getPersonsObject();
        Asserts.assertDeepEquals(actual, expected);
    }
}
