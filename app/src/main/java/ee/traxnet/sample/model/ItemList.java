package ee.traxnet.sample.model;

import java.io.Serializable;

import ee.traxnet.sample.type.ListItemType;

public class ItemList implements Serializable {
    public ListItemType listItemType;
    public String id;
    public String title;
}
