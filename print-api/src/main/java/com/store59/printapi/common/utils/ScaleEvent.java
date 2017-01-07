package com.store59.printapi.common.utils;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.*;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/2/27
 * @since 1.0
 */
public class ScaleEvent extends PdfPageEventHelper {
    protected float scale = 1;
    protected PdfDictionary pageDict;

    public ScaleEvent(float scale) {
        this.scale = scale;
    }

    public void setPageDict(PdfDictionary pageDict) {
        this.pageDict = pageDict;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        PdfDictionary pdfDictionary = new PdfDictionary();
        pdfDictionary.put(PdfName.ROTATE, pageDict.getAsNumber(PdfName.ROTATE));
        pdfDictionary.put(PdfName.MEDIABOX, scaleDown(pageDict.getAsArray(PdfName.MEDIABOX), scale));
        pdfDictionary.put(PdfName.CROPBOX, scaleDown(pageDict.getAsArray(PdfName.CROPBOX), scale));
        writer.setGroup(pdfDictionary);
    }

    public PdfArray scaleDown(PdfArray original, float scale) {
        if (original == null)
            return null;
        float width = original.getAsNumber(2).floatValue()
                - original.getAsNumber(0).floatValue();
        float height = original.getAsNumber(3).floatValue()
                - original.getAsNumber(1).floatValue();
        return new PdfRectangle(width * scale, height * scale);
    }

}
