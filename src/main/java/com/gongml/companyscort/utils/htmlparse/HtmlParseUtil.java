package com.gongml.companyscort.utils.htmlparse;

import com.gongml.companyscort.utils.groovy.GroovyContext;
import com.gongml.companyscort.utils.htmlparse.bean.HtmlLinkRule;
import com.gongml.companyscort.utils.htmlparse.bean.HtmlParseRule;
import com.gongml.companyscort.utils.htmlparse.bean.HtmlPathRule;
import com.gongml.companyscort.utils.htmlparse.bean.HtmlRowRule;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.xsoup.Xsoup;

import java.util.*;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-26 09:03
 **/
@Component
public class HtmlParseUtil {
    @Autowired
    private GroovyContext groovyContext;

    public List<Map<String, Object>> extractHtml(Map<String, Map<String, Object>> extractDate) {
        return null;
    }

    public List<Map<String, Object>> extractLink(Map<String, List<Map<String, Object>>> extractTable, HtmlParseRule htmlParseRule) {
        for (Map.Entry<String, List<Map<String, Object>>> entry : extractTable.entrySet()) {
            String key = entry.getKey();
            List<Map<String, Object>> tableValue = entry.getValue();
            List<HtmlRowRule> rowRules = htmlParseRule.getRowRules();
            for (HtmlRowRule rowRule : rowRules) {
                if (key.equals(rowRule.getRowColumnName())) {
                    HtmlLinkRule linkRule = rowRule.getLinkRule();

                }
            }
        }
        return null;
    }

    public Map<String, List<Map<String, Object>>> extractTable(Map<String, Map<String, Object>> extractDate) {
        Map<String, List<Map<String, Object>>> extractTableMap = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : extractDate.entrySet()) {
            String key = entry.getKey();
            Map<String, Object> dateMap = entry.getValue();
            List<Map<String, Object>> tableMapList = new ArrayList<>();
            int rowSize = 0;
            for (Map.Entry<String, Object> dateEntry : dateMap.entrySet()) {
                Object value = dateEntry.getValue();
                if (value != null) {
                    continue;
                }
                if (value instanceof List) {
                    List tempList = (List) value;
                    rowSize = tempList.size() > rowSize ? tempList.size() : rowSize;
                } else {
                    rowSize = rowSize > 1 ? rowSize : 1;
                }
            }
            for (int i = 0; i < rowSize; i++) {
                tableMapList.add(new LinkedHashMap<String, Object>());
            }
            for (Map.Entry<String, Object> dateEntry : dateMap.entrySet()) {
                Object dateValue = dateEntry.getValue();
                if (dateValue instanceof List) {
                    List<Map<String, Object>> tempList = (List<Map<String, Object>>) dateValue;
                    int colSize = getColSize(tableMapList);
                    for (int i = 0; i < tempList.size(); i++) {
                        Map<String, Object> tableMap = tableMapList.get(i);
                        Map<String, Object> tempMap = tempList.get(i);
                        int tempSize = colSize;
                        for (Map.Entry tempEntry : tempMap.entrySet()) {
                            tempSize++;
                            tableMap.put("col" + tempSize, tempEntry.getValue());
                        }
                    }
                }
            }
            cleanUpMapList(tableMapList);
            for (Map.Entry<String, Object> dateEntry : dateMap.entrySet()) {
                String dateKey = dateEntry.getKey();
                Object dateValue = dateEntry.getValue();
                if (!(dateValue instanceof List)) {
                    for (int i = 0; i < tableMapList.size(); i++) {
                        Map<String, Object> tableMap = tableMapList.get(i);
                        tableMap.put(dateKey, dateValue);
                    }
                }
            }
            extractTableMap.put(key, tableMapList);
        }
        return extractTableMap;
    }

    public int getColSize(List<Map<String, Object>> maplist) {
        if (CollectionUtils.isEmpty(maplist)) {
            return 0;
        }
        int nums = 0;
        for (String key : maplist.get(0).keySet()) {
            int colRealSize = getColRealSize(key);
            nums = nums > colRealSize ? nums : colRealSize;
        }
        return nums;
    }

    public int getColRealSize(String colName) {
        String string = colName.replace("col", "");
        return Integer.parseInt(string);

    }

    public Map<String, Map<String, Object>> extractDateByXpath(HtmlParseRule htmlParseRule, String htmlSource) throws Exception {
        if (StringUtils.isEmpty(htmlSource)) {
            return null;
        }
        List<HtmlRowRule> rowRules = htmlParseRule.getRowRules();
        Map<String, Map<String, Object>> parseRowMap = new LinkedHashMap<>();
        for (HtmlRowRule rowRule : rowRules) {
            String rowColumnName = rowRule.getRowColumnName();
            List<HtmlPathRule> pathRules = rowRule.getPathRules();
            Map<String, Object> dataMap = new LinkedHashMap<>();
            parseRowMap.put(rowColumnName, dataMap);
            for (HtmlPathRule pathRule : pathRules) {
                String xpath = pathRule.getXpath();
                String columnName = pathRule.getColumnName();
                Integer ruleType = pathRule.getRuleType();
                String function = pathRule.getFunction();
                String filter = pathRule.getFilter();
                if (StringUtils.isEmpty(xpath)) {
                    objectExpr(dataMap, columnName, function, null);
                    continue;
                }
                Document doc = Jsoup.parse(htmlSource);
                Elements eles = Xsoup.compile(xpath).evaluate(doc).getElements();
                if (eles == null || eles.size() < 1) {
                    objectExpr(dataMap, columnName, function, null);
                    continue;
                }
                String tagName = eles.get(0).tagName().toUpperCase();
                switch (ruleType) {
                    //html
                    case 1:
                        objectExpr(dataMap, columnName, function, eles.first().toString());
                        break;
                    //table
                    case 2:
                        if (StringUtils.indexOfAny(tagName, new String[]{"TABLE", "TBODY"}) != -1) {
                            listExpr(dataMap, columnName, function, grabTable(eles.get(0), filter, false));
                        } else if (StringUtils.indexOfAny(tagName, new String[]{"UL", "OL", "DL"}) != -1) {
                            listExpr(dataMap, columnName, function, grabListAsTable(eles.get(0), filter));
                        }
                        break;
                    //link
                    case 3:
                        if (StringUtils.indexOfAny(tagName, new String[]{"TABLE", "TBODY"}) != -1) {
                            listExpr(dataMap, columnName, function, grabTable(eles.get(0), filter, true));
                        } else if (StringUtils.indexOfAny(tagName, new String[]{"UL", "OL", "DL"}) != -1) {
                            listExpr(dataMap, columnName, function, grabListAsTable(eles.get(0), filter));
                        } else {
                            listExpr(dataMap, columnName, function, grabLink(eles.get(0)));
                        }
                        break;
                    default:
                        objectExpr(dataMap, columnName, function, eles.first().text());
                }
            }
        }
        return parseRowMap;
    }

    private void listExpr(Map<String, Object> dataMap, String columnName, String function, Object value) {
        if (value == null || StringUtils.isEmpty(function)) {
            dataMap.put(columnName, value);
            return;
        }
        List<Map<String, Object>> tempList = (List<Map<String, Object>>) value;
        String[] fomulars = function.split(";");
        for (int k = 0; k < fomulars.length; k++) {
            String col = fomulars[k].split("#")[0];
            String fomu = fomulars[k].split("#")[1];
            // 给每一行的相应列赋值
            for (Map<String, Object> map : tempList) {
                String name = null;
                try { // 函数的运算
                    Map<String, String> args = new HashMap<>();
                    name = (String) map.get(col);
                    args.put(col, name);
                    name = groovyContext.execExpr(fomu, args, String.class);
                    map.put(col, name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        dataMap.put(columnName, value);
    }

    private void objectExpr(Map<String, Object> dataMap, String columnName, String function, Object value) {
        Map<String, String> args = new HashMap<>();
        args.put(columnName, value == null ? null : value.toString());
        String resoult = null;
        if (StringUtils.isNotEmpty(function)) {
            resoult = groovyContext.execExpr(function, args, String.class);
        }
        dataMap.put(columnName, resoult);
    }

    private Elements getElements(Element ele, String... selector) {
        for (String select : selector) {
            Elements element = ele.select(select);
            if (element != null && element.size() > 0) {
                return element;
            }
        }
        return null;
    }

    private Elements addElements(Element ele, String... selector) {
        Elements ret = null;
        for (String select : selector) {
            Elements element = ele.select(select);
            if (element != null && element.size() > 0) {
                if (ret != null) {
                    ret.addAll(element);
                } else {
                    ret = element;
                }
            }
        }
        return ret;
    }

    private void deleteRowSpan(Elements cols) {
        int minRowSpan = Integer.MAX_VALUE;
        int rowSpan;
        for (int j = 0; j < cols.size(); j++) {
            Element cell = cols.get(j);
            rowSpan = 1;
            if (!cell.attr("rowspan").trim().isEmpty()) {
                rowSpan = Integer.parseInt(cell.attr("rowspan"));
            }
            minRowSpan = minRowSpan > rowSpan ? rowSpan : minRowSpan;
        }
        int deleteRowSpanNumber = minRowSpan - 1;
        if (deleteRowSpanNumber > 0) {
            for (int j = 0; j < cols.size(); j++) {
                Element cell = cols.get(j);
                rowSpan = Integer.parseInt(cell.attr("rowspan"));
                cell.attr("rowspan", (rowSpan - deleteRowSpanNumber) + "");
            }
        }
    }

    public String loadText(Node node) {
        StringBuilder accum = new StringBuilder();
        traverse(node, accum);
        return accum.toString().trim();
    }

    /**
     * @param root
     * @param builder
     * @author:gongml
     * @datetime:2017年4月11日 下午1:27:01
     * @desc:
     */
    public void traverse(Node root, StringBuilder builder) {
        Node node = root;
        int depth = 0;
        while (node != null) {
            head(node, builder);
            if (node.childNodeSize() > 0) {
                node = node.childNode(0);
                depth++;
            } else {
                while (node.nextSibling() == null && depth > 0) {
                    node = node.parentNode();
                    depth--;
                }
                if (node == root) {
                    break;
                }
                node = node.nextSibling();
            }
        }
    }

    public void head(Node node, StringBuilder builder) {
        if (node instanceof TextNode) {
            TextNode textNode = (TextNode) node;
            appendNormalisedText(builder, textNode);
        } else if (node instanceof Element) {
            Element element = (Element) node;
            if (builder.length() > 0 && (element.isBlock() || element.tagName().equals("br"))
                    && !lastCharIsWhitespace(builder)) {
                builder.append(",");
            }

        }
    }

    private boolean lastCharIsWhitespace(StringBuilder sb) {
        return sb.length() != 0 && (sb.charAt(sb.length() - 1) == ',' || sb.charAt(sb.length() - 1) == ' ');
    }

    private void appendNormalisedText(StringBuilder accum, TextNode textNode) {
        String text = textNode.getWholeText();
        if (preserveWhitespace(textNode.parentNode())) {
            accum.append(text);
        } else {
            StringUtil.appendNormalisedWhitespace(accum, text, lastCharIsWhitespace(accum));
        }

    }

    private boolean preserveWhitespace(Node node) {
        // looks only at this element and one level up, to prevent recursion &
        // needless stack searches
        if (node != null && node instanceof Element) {
            Element element = (Element) node;
            return element.tag().preserveWhitespace()
                    || element.parent() != null && element.parent().tag().preserveWhitespace();
        }
        return false;
    }

    private List<Map<String, Object>> filterRow(List<Element> trList, String prefix, List<Map<String, Object>> table) {
        List<Map<String, Object>> deleteList = new ArrayList<>();
        for (int i = 0; i < trList.size(); i++) {
            // first row is the col names so
            Element row = trList.get(i);
            if (row == null || row.childNodeSize() == 0 || "".equals(row.text().trim())) {
                deleteList.add(table.get(i));
                continue;
            }
            if (!StringUtils.isEmpty(prefix)) {
                String tString = row.text().replace(Jsoup.parse("&nbsp;").text(), " ").trim();
                try {
                    LinkedHashMap<String, String> args = new LinkedHashMap<>();
                    args.put("tr", tString);
                    boolean filt = groovyContext.parseExpr(prefix, args, Boolean.class);
                    if (filt) {
                        deleteList.add(table.get(i));
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return deleteList;
    }

    public void cleanUpMapList(List<Map<String, Object>> retrunTableMapList) {
        java.util.List<String> tempList = new ArrayList<>();
        //找到所有key值
        for (int i = 0; i < retrunTableMapList.size(); i++) {
            for (String key : retrunTableMapList.get(i).keySet()) {
                if (!tempList.contains(key)) {
                    tempList.add(key);
                }
            }
        }
        // 防止空指针的发生，将即将插入到表格的maplist填充
        for (int j = 0; j < retrunTableMapList.size(); j++) {
            for (int k = 0; k < tempList.size(); k++) {
                if (retrunTableMapList.get(j).get(tempList.get(k)) == null) {
                    retrunTableMapList.get(j).put(tempList.get(k), " ");
                }
            }
        }
    }

    public List<Map<String, Object>> sortTable(List<Map<String, Object>> table) {
        if (CollectionUtils.isEmpty(table) || MapUtils.isEmpty(table.get(0))) {
            return new ArrayList<Map<String, Object>>();
        }
        List<Map<String, Object>> sortTable = new ArrayList<>();
        for (int i = 0; i < table.size(); i++) {
            sortTable.add(new LinkedHashMap<String, Object>());
        }
        List<String> keyList = new ArrayList<String>();
        for (String key : table.get(0).keySet()) {
            keyList.add(key);
        }
        keyList.sort(new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                if (a != null && b != null) {
                    if (a.length() > b.length()) {
                        return 1;
                    }
                    if (a.length() < b.length()) {
                        return -1;
                    }
                    return a.compareTo(b);
                }
                return 0;
            }
        });
        for (int i = 0; i < sortTable.size(); i++) {
            for (String key : keyList) {
                sortTable.get(i).put(key, table.get(i).get(key));
            }
        }
        return sortTable;
    }

    public List<Map<String, Object>> grabTable(Element ele, String filterScript, boolean isLink) {
        Elements tableElement = getElements(ele, "tbody", "table");
        Elements rows = null;
        if (getElements(ele, "thead") != null) {
            rows = getElements(ele, "thead");
        }
        if (rows != null && rows.size() > 0) {
            rows.addAll(tableElement.first().select("> tr"));
        } else {
            rows = tableElement.first().select("> tr");
        }
        if (getElements(ele, "tfoot") != null && getElements(ele, "tfoot").first().select("> tr") != null) {
            rows.addAll(getElements(ele, "tfoot").first().select("> tr"));
        }
        int savecolnums = 0;
        List<Elements> tdsList = new ArrayList<Elements>();
        List<Element> trList = new ArrayList<Element>();
        for (int i = 0; i < rows.size(); i++) {
            Element tr = rows.get(i);
            Elements tds = null;
            int nums = 0;
            if (tr.select("tr").size() > 0) {
                tr.attr("id", "TR_ONLY");
                tds = addElements(tr, "th", "#TR_ONLY > td");
            } else {
                tds = addElements(tr, "th", "> td");
            }
            trList.add(tr);
            tdsList.add(tds);
            if (tds != null) {
                //防止表头和表内容合并时出现 表头列数减少导致表不完整的情况
                for (int j = 0; j < tds.size(); j++) {
                    if (!tds.get(j).attr("colspan").trim().isEmpty()) {
                        String colspan = tds.get(j).attr("colspan");
                        int colSpan = Integer.parseInt(colspan);
                        nums += colSpan;
                    } else {
                        nums++;
                    }
                }
                savecolnums = nums > savecolnums ? nums : savecolnums;
            }
        }
        //创建表格实体
        List<Map<String, Object>> table = new ArrayList<>();
        for (int i = 0; i < tdsList.size(); i++) {
            table.add(new LinkedHashMap<String, Object>());
        }
        List<Map<String, Object>> rowMapList = new ArrayList<>();
        for (int i = 0; i < tdsList.size(); i++) {
            Elements cols = tdsList.get(i);
            if (cols != null) {
                deleteRowSpan(cols);
                // 表格解析每行数据
                for (int j = 0; j < cols.size(); j++) {
                    Element cell = cols.get(j);
                    int colSpan = 1;
                    int rowSpan = 1;
                    if (!cell.attr("rowspan").trim().isEmpty()) {
                        rowSpan = Integer.parseInt(cols.get(j).attr("rowspan"));
                    }
                    if (!cols.get(j).attr("colspan").trim().isEmpty()) {
                        colSpan = Integer.parseInt(cols.get(j).attr("colspan"));
                    }
                    // 解析获取a标签与文本内容
                    String link = null;
                    String text = null;
                    {
                        if (!cell.select("a").isEmpty()) {
                            Elements elements = cell.select("a");
                            StringBuffer stringText = new StringBuffer("");
                            StringBuffer stringLink = new StringBuffer("");
                            for (Element element : elements) {
                                if (element.attr("onClick") != null && !element.attr("onClick").trim().equals("")) {
                                    stringLink.append(element.attr("onClick"));
                                } else if (element.attr("href") != null) {
                                    stringLink.append(element.attr("href"));
                                }
                                stringText.append(element.attr("title").isEmpty()
                                        ? (element.attr("src").isEmpty() ? element.text() : element.attr("src"))
                                        : element.attr("title"));
                                if (stringLink.length() != 0 && stringLink.charAt(stringLink.length() - 1) != '$') {
                                    stringLink.append("$");
                                }
                                if (stringText.length() != 0 && stringText.charAt(stringText.length() - 1) != '$') {
                                    stringText.append("$");
                                }
                            }
                            if (stringLink.length() != 0) {
                                stringLink = stringLink.deleteCharAt(stringLink.length() - 1);
                            }
                            if (stringText.length() != 0) {
                                stringText = stringText.deleteCharAt(stringText.length() - 1);
                            }
                            link = stringLink.toString();
                            text = stringText.toString();
                        } else if (!cell.select("img").isEmpty()) {
                            StringBuffer stringText = new StringBuffer("");
                            StringBuffer stringLink = new StringBuffer("");
                            if (!cell.text().isEmpty()) {
                                stringText.append(cell.text()).append("$");
                            }
                            Elements elements = cell.select("img");
                            for (Element element : elements) {
                                if (element.attr("onClick") != null && !element.attr("onClick").trim().equals("")) {
                                    stringLink.append(element.attr("onClick"));
                                } else if (element.attr("href") != null) {
                                    stringLink.append(element.attr("href"));
                                }
                                stringText.append(element.text());
                                if (element.text().trim().isEmpty() && !element.attr("src").isEmpty()) {
                                    stringText.append(element.attr("src"));
                                }
                                if (stringLink.length() != 0 && stringLink.charAt(stringLink.length() - 1) != '$') {
                                    stringLink.append("$");
                                }
                                if (stringText.length() != 0 && stringText.charAt(stringText.length() - 1) != '$') {
                                    stringText.append("$");
                                }
                            }
                            if (stringLink.length() != 0) {
                                stringLink = stringLink.deleteCharAt(stringLink.length() - 1);
                            }
                            if (stringText.length() != 0) {
                                stringText = stringText.deleteCharAt(stringText.length() - 1);
                            }
                            link = stringLink.toString();
                            text = stringText.toString();
                        } else {
                            // td下没有<a>但是本身具有onclick属性
                            if (!cell.attr("onclick").isEmpty()) {
                                link = cell.attr("onclick");
                            }
                            if (cell.text().isEmpty()) {
                                text = "";
                            } else {
                                // .replace(Jsoup.parse("&nbsp;").text(), "
                                // ").trim()
                                text = loadText(cell);
                            }
                        }
                        // 表格td下 标签复杂 采取将分布提取 第一步将链接提出（一般会有 标题和链接列）； 第二步(不勾选链接)将
                        // 内容提出、采取公司分割成不同的列 最后进行合并
                        if (text != null && cell.text().length() > text.length()) {
                            text = cell.text();
                        }
                    }
                    for (int temp = 0; temp < rowSpan; temp++) {
                        rowMapList.add(table.get(i + temp));
                    }
                    int columns = 0;
                    while (colSpan > 0) {
                        for (Map<String, Object> rowMapDate : rowMapList) {
                            if (rowMapDate == rowMapList.get(0)) {
                                // 当有跨行时，跳过已有数据的列
                                while (rowMapDate.get("col" + columns) != null) {
                                    columns++;
                                }
                                if (text != null) {
                                    rowMapDate.put("col" + columns, text);
                                }
                                if (isLink && link != null) {
                                    rowMapDate.put("col" + (columns + savecolnums), link);
                                }
                            } else {
                                while (rowMapDate.get("col" + columns) != null) {
                                    columns++;
                                }
                                if (text != null) {
                                    rowMapDate.put("col" + columns, "");
                                }
                                if (isLink && link != null) {
                                    rowMapDate.put("col" + (columns + savecolnums), "");
                                }
                            }
                        }
                        colSpan--;
                        columns++;
                    }
                    rowMapList.clear();
                }
            }
        }
        List<Map<String, Object>> deleteList = filterRow(trList, filterScript, table);
        table.removeAll(deleteList);
        cleanUpMapList(table);
        return sortTable(table);
    }

    public List<Map<String, Object>> grabListAsTable(Element ele, String prefix) {
        List<Map<String, Object>> maplist = new ArrayList<>();
        Elements dliElement = getElements(ele, "ul", "dl");
        List<String> tagNames = new ArrayList<>();
        int firstLine = 0;
        for (int i = 0; i < dliElement.size(); i++) {
            Element element = dliElement.get(i);
            Elements eles = getElements(element, "li", "dd");
            if (eles == null) {
                return null;
            }
            for (int j = 0; j < eles.size(); j++) {
                Map<String, Object> map = new LinkedHashMap<>();
                Element ele2 = eles.get(j);
                if (!StringUtils.isEmpty(prefix)) {
                    String tString = ele2.html();
                    try {
                        Map<String, String> args = new LinkedHashMap<>();
                        args.put("tr", tString);
                        boolean filt = groovyContext.parseExpr(prefix, args, Boolean.class);
                        if (filt) {
                            continue;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                List<Node> list = ele2.childNodes();
                if (list == null) {
                    continue;
                }
                int index = -1;
                int tagNum = 0;
                for (Node node : list) {
                    int nums = index;
                    if (node instanceof Element) {
                        Element enode = (Element) node;
                        Element ea = enode.select("a").first();
                        if (ea != null) {
                            String href = ea.attr("href");
                            map.put("col" + (++index), href);
                        }
                        if (!enode.text().isEmpty()) {
                            map.put("col" + (++index), enode.text());
                            // continue;
                        } else if (!enode.select("img").attr("title").isEmpty()) {
                            map.put("col" + (++index), enode.select("img").attr("title"));
                            // continue;
                        }
                        if (ea != null && StringUtils.isNotEmpty(ea.attr("title"))) {
                            map.put("col" + (++index), ea.attr("title"));
                            // continue;
                        }
                    } else if (node instanceof TextNode) {
                        TextNode tn = (TextNode) node;
                        if (!"".equals(tn.text().trim())) {
                            map.put("col" + (++index), tn.text().trim());
                        }
                    }
                    if (firstLine == 0) {
                        tagNames.add(node.nodeName());
                    } else {
                        if (tagNames.size() - 1 < tagNum || !tagNames.get(tagNum++).equals(node.nodeName())) {
                            if (tagNames.size() < list.size()) {
                                // 上少下多
                                Map<String, Object> map2 = maplist.get(maplist.size() - 1);
                                for (int k = 0; k < index - nums; k++) {
                                    map2.put("col" + (index + k + 1), map2.get("col" + (nums + k + 1)));
                                    map2.put("col" + (nums + k + 1), "");
                                }
                                tagNames.add(tagNum - 1, node.nodeName());
                            } else if (tagNames.size() > list.size()) {
                                // 上多下少
                                for (int k = 0; k < index - nums; k++) {
                                    map.put("col" + (index + k + 1), map.get("col" + (nums + k + 1)));
                                    map.put("col" + (nums + k + 1), "");
                                }
                            }
                        }
                    }
                }
                firstLine = j + 1;
                maplist.add(map);
            }
        }
        cleanUpMapList(maplist);
        return maplist;
    }

    private void getLinkTitle(Element e, StringBuffer str) {
        if (e.previousElementSibling() != null) {
            alinkPreviousEle(e, str);
        }
        if (e.nextElementSibling() != null) {
            alinkNextEle(e, str);
        }
    }

    private void alinkPreviousEle(Element e, StringBuffer str) {
        if (e.previousElementSibling() != null) {
            Element preEle = e.previousElementSibling();
            if ("A".equalsIgnoreCase(preEle.tagName())) {
                return;
            }
            if (preEle.text() != null && !preEle.text().isEmpty()) {
                str.insert(0, preEle.text() + "&");
            }
            alinkPreviousEle(preEle, str);
        }
    }

    private void alinkNextEle(Element e, StringBuffer str) {
        if (e.nextElementSibling() != null) {
            Element nextEle = e.nextElementSibling();
            if ("A".equalsIgnoreCase(nextEle.tagName())) {
                return;
            }
            if (nextEle.text() != null && !nextEle.text().isEmpty()) {
                str.append(nextEle.text() + "&");
            }
            alinkPreviousEle(nextEle, str);
        }
    }

    public List<Map<String, Object>> grabLink(Element element) throws Exception {
        Elements elements = element.select("a");
        List<Map<String, Object>> result = new ArrayList<>();
        for (Element eachEle : elements) {
            if (!eachEle.hasText() || !eachEle.hasAttr("href")) {
                // 如果链接没有文本内容或href属性则跳过
                continue;
            }
            String link = eachEle.attr("href");
            StringBuffer title = new StringBuffer(eachEle.text());
            getLinkTitle(eachEle, title);
            String strTitle = title.toString();
            Map<String, Object> linkMap = new LinkedHashMap<String, Object>();
            Element parent = eachEle.parent();
            String parentText = "";
            if (parent != null) {
                parentText = parent.text();
            }
            while (strTitle.equals(parentText)) {
                parent = parent.parent();
                if (parent != null) {
                    parentText = parent.text();
                } else {
                    break;
                }
            }
            // 替换全角的空格为半角空格
            parentText = parentText.replaceAll("[\\p{Zs}]", " ");
            linkMap.put("col0", strTitle);
            linkMap.put("col1", link);
            linkMap.put("col2", parentText.replace(strTitle, "").trim());
            if (eachEle.hasAttr("title")) {
                linkMap.put("col3", eachEle.attr("title"));
            }
            result.add(linkMap);
        }
        return result;
    }
}
