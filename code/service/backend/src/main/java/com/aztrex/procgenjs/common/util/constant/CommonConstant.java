package com.aztrex.procgenjs.common.util.constant;

import java.util.HashSet;
import java.util.Set;

public class CommonConstant {
    public static final Set<String> IGNORE_PARAMETER_SET = new HashSet<String>(Set.of("page", "size", "sort"));
    public static final Set<String> UUID_PARAMETER_SET = new HashSet<String>(Set.of("projectId", "collectionId"));

    // symbols start
    public static final String PERCENT = "%";
    // symbols stop

    // words start
    public static final String NAME = "name";
    public static final String DETAILS = "details";
    public static final String NOTES = "notes";
    public static final String TYPE = "type";
    public static final String CATEGORY = "category";
    public static final String SUBCATEGORY = "subcategory";
    public static final String GROUP = "group";
    public static final String SUBGROUP = "subgroup";
    public static final String SECTION = "section";
    public static final String TAGS = "tags";
    public static final String LINKS = "links";
    public static final String USED = "used";

    public static final String SPACE = " ";
    public static final String SELECT = "select";
    public static final String DELETE = "delete";
    public static final String UPDATE = "update";
    public static final String INSERT = "insert";
    public static final String CREATE = "create";
    public static final String CONTENT = "content";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String TOTAL_ITEMS = "totalItems";
    public static final String TOTAL_PAGES = "totalPages";

    public static final String READ = "read";
    public static final String WRITE = "write";


    public static String ID = "id";
    public static String WS_ID = "wsid";
    public static String BASE = "base";
    public static String TITLE = "title";
    public static String DIRECTORY = "directory";
    public static String DESCRIPTION = "description";
    public static String SELECTED = "selected";
    public static String DEFAULT = "default";
    public static String DATE_CREATED = "dateCreated";
    public static String DATE_UPDATED = "dateUpdated";
    public static String CREATED = "created";
    public static String ROOT_FOLDERS = "Root Folders";
    public static String SCALE = "scale";
    public static String SCALE_FACTOR = "scaleFactor";
    public static String DATA = "data";
    public static String TEMP_DATA = "tempData";
    public static String CanvasScaleFactor = "canvasScaleFactor";
    public static String ImageSettings = "Image Settings";
    public static String TAB_STATE = "tabState";
    public static String JDBC_SQLITE = "jdbc:sqlite:";

    // words stop

    // file extension start
    public static final String Empty = "";
    public static final String Space = " ";
    public static final String DOT = ".";
    public static final String JAVA_EXTENSION = ".java";
    public static final String CLASS_EXTENSION = ".class";
    public static final String PNG_EXTENSION = ".png";
    public static final String JSON_EXTENSION = ".json";

    public static final String PNG = "png";
    // file extension stop
}
