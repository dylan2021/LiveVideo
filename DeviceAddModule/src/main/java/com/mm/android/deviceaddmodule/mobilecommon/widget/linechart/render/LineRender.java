package com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;

import com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.animate.LAnimator;
import com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.charts.LineChart;
import com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.data.Entry;
import com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.data.Line;
import com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.data.Lines;
import com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.manager.MappingManager;
import com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.utils.SingleF_XY;

import java.util.List;


public class LineRender extends BaseRender {

    LineChart lineChart;
    Lines _lines;

    Paint _PaintLine;
    Paint _PaintCircle;
    Paint _PaintLegend;

    Path _PathLine;

    LAnimator _LAnimator;

    Bitmap _drawBitmap;
    Canvas softwareCanvas = new Canvas();

    public LineRender(RectF rectMain, MappingManager _MappingManager, Lines _lines, LineChart lineChart) {
        super(rectMain, _MappingManager);
        this._lines = _lines;
        this.lineChart = lineChart;
        this._LAnimator = lineChart.get_LAnimator();

        _PaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLegend = new Paint(Paint.ANTI_ALIAS_FLAG);

        _PathLine = new Path();
    }


    public void onChartSizeChanged(int w, int h) {
        if (w > 0 && h > 0) {
            _drawBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            softwareCanvas.setBitmap(_drawBitmap);
        }
    }


    public void render(Canvas canvasSrc) {

        if (_lines == null) {
            return;
        }

        Canvas canvas = null;

        // layout editor 状态下 _drawBitmap 是没有的
        if (null != _drawBitmap) {
            canvas = softwareCanvas;
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        } else {
            canvas = canvasSrc;
        }


        canvas.save();
//        canvas.clipRect(_rectMain); // 限制绘制区域

        RectF rectF = new RectF(_rectMain);
        rectF.top = _rectMain.top - 20;
        rectF.bottom = _rectMain.bottom + 20;
        canvas.clipRect(rectF); // 限制绘制区域

        // 绘制折线 和 折线上的圆点
        for (Line line : _lines.getLines()) {
            if (!line.isEnable()) {
                continue;
            }
            drawLine_Circle(canvas, line);
        }

        canvas.restore();

        // 绘制每条线的 legend
//        for (int i = 0; i < _lines.getLines().size(); i++) {
//            Line line = _lines.getLines().get(i);
//            if (!line.isEnable()) {
//                continue;
//            }
//            drawLegend(canvas, line, i);
//        }

        if (_drawBitmap != null) {
            canvasSrc.drawBitmap(_drawBitmap, 0, 0, null);
        }
    }

    private void drawLine_Circle(Canvas canvas, Line line) {

//        HighLight highLight = new HighLight();
//        highLight.setHighLightColor(line.getLineColor());

        _PaintLine.setStrokeWidth(line.getLineWidth());
        _PaintLine.setColor(line.getLineColor());
        _PaintLine.setStyle(Paint.Style.STROKE);

        _PaintCircle.setStrokeWidth(line.getLineWidth());
        _PaintCircle.setColor(line.getLineColor());

        List<Entry> list = line.getEntries();

        // check
        if (list == null || list.size() == 0) {
            return;
        }

        // 考虑只有一个点的情况
        if (list.size() == 1) {
            Entry hit = list.get(0);
            SingleF_XY xy = _MappingManager.getPxByEntry(hit);
            canvas.drawCircle(xy.getX(), xy.getY(), line.getCircleR(), _PaintCircle);
            return;
        }

        double xMin_Visiable = lineChart.getVisiableMinX();
        double xMax_Visiable = xMin_Visiable + (lineChart.getVisiableMaxX() - xMin_Visiable) * _LAnimator.get_hitValueX();// 考虑动画

        int minIndex = Line.getEntryIndex(list, xMin_Visiable, Line.Rounding.DOWN);
        int maxIndex = Line.getEntryIndex(list, xMax_Visiable, Line.Rounding.UP);

        if (Math.abs(maxIndex - minIndex) == 0) {
            return;
        }

        if ((maxIndex - minIndex) > 1500) {
            _PaintLine.setAntiAlias(false);// 考虑性能
        } else {
            _PaintLine.setAntiAlias(true);
        }

        _PathLine.reset();

        int breakI = -1;

        for (int i = minIndex; i <= maxIndex; i++) {
            Entry entry = list.get(i);

            // 考虑这个点是否断掉
            if (entry.isNull_Y()) {
                breakI = i;
                continue;
            }

            float sx = _MappingManager.v2p_x(entry.getX());
            float sy = getAnimateY(_MappingManager.v2p_y(entry.getY()));

            if (i == minIndex || i == breakI + 1) {
                _PathLine.moveTo(sx, sy);
            } else {
                _PathLine.lineTo(sx, sy);
            }

            if (line.isDrawCircle()) {
                SingleF_XY xy = _MappingManager.getPxByEntry(entry);
                canvas.drawCircle(xy.getX(), getAnimateY(xy.getY()), line.getCircleR(), _PaintCircle);

                // 把最后点一个绘制出来
                if (i == maxIndex - 1) {
                    Entry last = list.get(maxIndex);
                    xy = _MappingManager.getPxByEntry(last);
                    /////////////////////////////////////////////  考虑动画效果  //////////////////////////////////////////
                    canvas.drawCircle(xy.getX(), getAnimateY(xy.getY()), line.getCircleR(), _PaintCircle);
                }
            }
        }

        canvas.drawPath(_PathLine, _PaintLine);

        // 有断掉的点，就不考虑填充了
        if (breakI != -1) {
            return;
        }

        // 考虑填充
        if (line.isFilled()) {
            Entry start = list.get(minIndex);
            Entry end = list.get(maxIndex);
            float sx = _MappingManager.v2p_x(start.getX());
            float sy = getAnimateY(_MappingManager.v2p_y(start.getY()));
            float ex = _MappingManager.v2p_x(end.getX());
            float ey = getAnimateY(_MappingManager.v2p_y(end.getY()));

            float maxY = Math.max(sy, ey);

            _PathLine.lineTo(ex, lineChart.get_MainPlotRect().bottom);
            _PathLine.lineTo(sx, lineChart.get_MainPlotRect().bottom);

            _PathLine.close();

            _PaintLine.setStyle(Paint.Style.FILL);
            _PaintLine.setAlpha(50);

            Shader shader = new LinearGradient(0, maxY, 0, 0, line.getLineColor(), line.getLineColor() / 50, Shader.TileMode.CLAMP);
            _PaintLine.setShader(shader);

            canvas.drawPath(_PathLine, _PaintLine);
            _PaintLine.setStyle(Paint.Style.STROKE);
            _PaintLine.setShader(null);
        }
    }


    RectF _RectFBuffer = new RectF();


    public void onDataChanged(Lines lines) {
        _lines = lines;
    }

    private float getAnimateY(float src) {
        return lineChart.get_MainPlotRect().bottom - (lineChart.get_MainPlotRect().bottom - src) * _LAnimator.get_hitValueY();
    }
}
