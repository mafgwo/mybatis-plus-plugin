package com.baomidou.plugin.idea.mybatisx.util;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.PlatformIcons;

import javax.swing.*;

/**
 * 图标
 */
public interface Icons {

    Icon MYBATIS_LOGO = IconLoader.getIcon("/images/logo.png");

    Icon PARAM_COMPLETION_ICON = PlatformIcons.PARAMETER_ICON;

    Icon MAPPER_LINE_MARKER_ICON = IconLoader.getIcon("/images/xml-mapper.png");

    Icon STATEMENT_LINE_MARKER_ICON = IconLoader.getIcon("/images/java.svg");

    Icon SPRING_INJECTION_ICON = IconLoader.getIcon("/images/java.svg");
}

