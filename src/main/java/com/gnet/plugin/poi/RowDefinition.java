package com.gnet.plugin.poi;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowDefinition implements Serializable, Cloneable {

    public static class ValidateDefinition implements Serializable {

        @Getter
        private final String[] checks;

        @Getter
        private final boolean allowEmpty;

        public ValidateDefinition(String[] checkes, boolean allowEmpty) {
            this.checks = checkes;
            this.allowEmpty = allowEmpty;
        }

        public ValidateDefinition(String[] checkes) {
            this(checkes, false);
        }

        /**
         * 检测是否在符合范围内
         *
         * @param value 需要检测的值
         * @return 检测是否成功
         */
        public boolean check(String value) {
            if (StringUtils.isBlank(value)) {
                if (allowEmpty) {
                    return true;
                }

                return false;
            }

            for (String check : checks) {
                if (check.equals(value)) {
                    return true;
                }
            }
            return false;
        }

        public String checksToString() {
            StringBuffer sb = new StringBuffer();
            for (String check : checks) {
                sb.append(check).append(",");
            }
            return sb.toString();
        }

    }

    @Getter
    @Setter
    public static class ColumnDefinition implements Serializable, Cloneable {
        public enum HEADER_COLUMN_TYPE {
            COMMON, GROUP
        }

        private HEADER_COLUMN_TYPE type;
        private int index;
        private String name;
        private Object value;
        private ValidateDefinition validateDefinition;
        private ValidateDefinition dataValidationDefinition;

        public ColumnDefinition(int index, HEADER_COLUMN_TYPE type, String name) {
            this.type = type;
            this.index = index;
            this.name = name;
        }

        public static ColumnDefinition createCommonColumn(int index, String name) {
            return new ColumnDefinition(index, HEADER_COLUMN_TYPE.COMMON, name);
        }

        public static GroupColumnDefinition createGroupColumn(int startIndex, int endIndex, String name) {
            return new GroupColumnDefinition(startIndex, endIndex, name);
        }

        public boolean isGroup() {
            return HEADER_COLUMN_TYPE.GROUP.equals(getType());
        }

        public boolean isHere(int cellIndex) {
            return this.getIndex() == cellIndex;
        }

        public ColumnDefinition setValidateDefinition(ValidateDefinition validateDefinition) {
            this.validateDefinition = validateDefinition;
            return this;
        }

        public ColumnDefinition setDataValidationDefinition(ValidateDefinition dataValidationDefinition) {
            this.dataValidationDefinition = dataValidationDefinition;
            return this;
        }

        public static ColumnDefinition copy(ColumnDefinition columnDefinition) {
            ColumnDefinition target = new ColumnDefinition(columnDefinition.getIndex(), columnDefinition.getType(), columnDefinition.getName());
            target.setValue(columnDefinition.getValue());
            return target;
        }

    }

    @Getter
    public static class GroupColumnDefinition extends ColumnDefinition {

        private List<ColumnDefinition> columns;
        private int endIndex;

        private Map<Integer, ColumnDefinition> indexs;

        public GroupColumnDefinition(int startIndex, int endIndex, String name) {
            super(startIndex, HEADER_COLUMN_TYPE.GROUP, name);
            this.endIndex = endIndex;
            this.columns = new ArrayList<>();
        }

        public GroupColumnDefinition addColumn(ColumnDefinition headerColumn) {
            if (this.indexs == null) {
                throw new RuntimeException("请先指定索引池，或者将其加入Header后再行操作");
            }
            this.columns.add(headerColumn);
            if (!headerColumn.isGroup()) {
                this.indexs.put(headerColumn.getIndex(), headerColumn);
            }
            return this;
        }

        @Override
        public boolean isHere(int cellIndex) {
            return getIndex() <= cellIndex || getEndIndex() >= cellIndex;
        }

        public GroupColumnDefinition setIndexs(Map<Integer, ColumnDefinition> indexs) {
            this.indexs = indexs;
            return this;
        }

        public static GroupColumnDefinition copy(Map<Integer, ColumnDefinition> indexes, GroupColumnDefinition columnDefinition) {
            GroupColumnDefinition groupTarget = new GroupColumnDefinition(columnDefinition.getIndex(), columnDefinition.getEndIndex(), columnDefinition.getName());
            groupTarget.setValue(columnDefinition.getValue());

            if (indexes == null) {
                indexes = new HashMap<>();
            }

            List<ColumnDefinition> targetColumns = groupTarget.getColumns();
            for (ColumnDefinition item : columnDefinition.getColumns()) {
                if (item.isGroup()) {
                    GroupColumnDefinition copied = GroupColumnDefinition.copy(indexes, (GroupColumnDefinition) item);
                    copied.setIndexs(indexes);
                    targetColumns.add(copied);
                } else {
                    ColumnDefinition copied = ColumnDefinition.copy(item);
                    targetColumns.add(copied);
                    indexes.put(copied.getIndex(), copied);
                }
            }

            return groupTarget;
        }

    }

    @Getter
    private GroupColumnDefinition groupColumnDefinition;
    @Getter
    private Map<Integer, ColumnDefinition> indexs;

    public RowDefinition(int endIndex) {
        this.groupColumnDefinition = new GroupColumnDefinition(0, endIndex, "all");
        this.indexs = new HashMap<>();
        this.groupColumnDefinition.setIndexs(this.indexs);
    }

    public RowDefinition addColumn(ColumnDefinition headerColumn) {
        if (headerColumn.isGroup()) {
            ((GroupColumnDefinition) headerColumn).setIndexs(this.indexs);
        }
        this.groupColumnDefinition.addColumn(headerColumn);
        return this;
    }

    public static RowDefinition copy(RowDefinition rowDefinition) {
        RowDefinition target = new RowDefinition(rowDefinition.getGroupColumnDefinition().getEndIndex());

        Map<Integer, ColumnDefinition> targetIndexs = target.indexs;
        target.groupColumnDefinition = GroupColumnDefinition.copy(targetIndexs, rowDefinition.groupColumnDefinition);
        target.groupColumnDefinition.setIndexs(targetIndexs);

        return target;
    }

}