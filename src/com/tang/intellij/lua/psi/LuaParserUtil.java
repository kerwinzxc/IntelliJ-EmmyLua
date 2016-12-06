package com.tang.intellij.lua.psi;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.impl.source.tree.ElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ArrayUtil;

public class LuaParserUtil extends GeneratedParserUtilBase {

    public static boolean twoExpr(PsiBuilder builder_, int level_, Parser parser) {
        System.out.println("twoExpr");
        PsiBuilder.Marker marker = builder_.mark();
        boolean r = parser.parse(builder_, level_);
        r = r && parser.parse(builder_, level_);
        marker.rollbackTo();
        return r;
    }

    public static boolean checkNext(PsiBuilder builder_, int level_, Parser parser) {
        PsiBuilder.Marker marker = builder_.mark();
        boolean r = parser.parse(builder_, level_);
        marker.rollbackTo();
        return r;
    }

    public static boolean lazyBlock(PsiBuilder builder_, int level_) {
        int i = 0;
        IElementType prev=  builder_.rawLookup(--i);
        while (prev == ElementType.WHITE_SPACE)
            prev = builder_.rawLookup(--i);

        if (prev != null) {
            PsiBuilder.Marker marker = builder_.mark();
            matchStart(builder_, level_, prev);
            marker.collapse(LuaTypes.BLOCK);
        }
        return true;
    }

    static void matchStart(PsiBuilder builder, int level, IElementType type) {
        if (type == LuaTypes.DO) matchEnd(builder, level, LuaTypes.END);
        if (type == LuaTypes.REPEAT) matchEnd(builder, level, LuaTypes.UNTIL);
    }

    static void matchEnd(PsiBuilder builder, int level, IElementType ... types) {
        IElementType type = builder.getTokenType();
        while (true) {
            if (type == null || builder.eof() || type == LuaTypes.END) {
                break;
            }
            if (ArrayUtil.indexOf(types, type) != -1)
                break;

            builder.advanceLexer();
            type = builder.getTokenType();
        }
    }
}
