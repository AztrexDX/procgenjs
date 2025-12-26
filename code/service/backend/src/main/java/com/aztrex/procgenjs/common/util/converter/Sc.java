package com.aztrex.procgenjs.common.util.converter;

import java.util.HashSet;
import java.util.Set;

public class Sc {
	public static final Set<String> IGNORE_PARAMETER_SET = new HashSet<String>(Set.of("page", "size", "sort"));
	public static final Set<String> UUID_PARAMETER_SET = new HashSet<String>(Set.of("projectId", "collectionId"));
	
	// symbols
	public static final String PERCENT = "%";
	
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

}
