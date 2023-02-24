package Tools.DxfReader;

import org.kabeja.dxf.*;
import org.kabeja.dxf.helpers.Point;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;
import wblut.geom.WB_Circle;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: dxfImport
 * @author: Naturalpowder; Alessio
 * @create: 2020-10-15 15:00
 **/
public class DXFImporter {
    public static final String GBK = "gbk", UTF_8 = "utf-8";

    /**
     * Example
     *
     * @param args
     */
    public static void main(String[] args) {
        DXFImporter importer = new DXFImporter("C:\\Users\\ldy90\\Desktop\\test.dxf", UTF_8);
        List<WB_PolyLine> lines = importer.getLines("0");
        List<WB_Point> points = importer.getPoints("0");
    }

    //**
    private final String filePath;
    private DXFDocument doc;
    private String encoding;

    public DXFImporter(String filePath, String encoding) {
        this.filePath = filePath;
        this.encoding = encoding;
        read();
    }

    public List<String> getLayers() {
        List<String> layers = new ArrayList<>();
        Iterator iterator = doc.getDXFLayerIterator();
        while (iterator.hasNext()) {
            layers.add(((DXFLayer) iterator.next()).getName());
        }
        return layers;
    }

    public List<WB_Point> getBlockCenter(String layerId) {
        List<DXFInsert> inserts = getBlock(layerId);
        List<WB_Point> centers = new ArrayList<>();
        if (inserts != null)
            for (Object o : inserts) {
                DXFInsert i = (DXFInsert) o;
                String name = i.getBlockID();
                double x = 0, y = 0;
                Iterator iterator = doc.getDXFBlock(name).getDXFEntitiesIterator();
                while (iterator.hasNext()) {
                    Object thisO = iterator.next();
                    if (thisO.getClass().equals(DXFText.class)) {
                        DXFText text = (DXFText) thisO;
                        x = text.getAlignX();
                        y = text.getAlignY();
                    } else if (thisO.getClass().equals(DXFCircle.class)) {
                        DXFCircle text = (DXFCircle) thisO;
                        x = text.getCenterPoint().getX();
                        y = text.getCenterPoint().getY();
                    } else if (thisO.getClass().equals(DXFPolyline.class)) {
                        DXFPolyline text = (DXFPolyline) thisO;
                        x = text.getVertex(0).getX();
                        y = text.getVertex(0).getY();
                    } else if (thisO.getClass().equals(DXFLine.class)) {
                        DXFLine text = (DXFLine) thisO;
                        x = text.getStartPoint().getX();
                        y = text.getStartPoint().getY();
                    }
                    WB_Point p = new WB_Point(x * i.getScaleX(), y * i.getScaleX());
                    p = p.rotateAboutOrigin2D(i.getRotate() / 180 * Math.PI);
                    WB_Point fp = new WB_Point(i.getPoint().getX() + p.xd(), i.getPoint().getY() + p.yd());
                    centers.add(fp);
                    break;
                }
            }
        return centers;
    }

    public List<DXFInsert> getBlock(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId);
        List list = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_INSERT);
        return list;
    }

    public List<WB_PolyLine> getSegmentBlock(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId);
        List<Z_PolyLine> segments = new ArrayList<>();
        List list = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_INSERT);
        System.out.println(layer.getName());
        System.out.println("读取到块的数量：" + list.size());
        for (Object o : list) {
            DXFInsert i = (DXFInsert) o;
            String name = i.getBlockID();
            double x = 0, y = 0;
            Iterator iterator = doc.getDXFBlock(name).getDXFEntitiesIterator();
            while (iterator.hasNext()) {
                Object thisO = iterator.next();
                DXFPolyline pLine = null;
                if (thisO.getClass().equals(DXFPolyline.class)) {
                    pLine = (DXFPolyline) thisO;
                    List<WB_Point> pts = new ArrayList<>();
                    for (int n = 0; n < pLine.getVertexCount(); n++) {
                        DXFVertex vertex = pLine.getVertex(n);
                        pts.add(new WB_Point(vertex.getX(), vertex.getY(), vertex.getZ()));
                    }
                    if (pLine.isClosed()) {
                        segments.add(new Z_PolyLine(null, new WB_Polygon(pts)));
                    } else {
                        segments.add(new Z_PolyLine(new WB_PolyLine(pts), null));
                    }
                }
            }
        }
        return segments.stream().filter(e -> e.getPolygon() != null).map(Z_PolyLine::getPolygon).collect(Collectors.toList());
    }

    public List<WB_Point> getCircleCenters(String layerId) {
        List<WB_Circle> circles = getCircles(layerId);
        List<WB_Point> centers = new ArrayList<>();
        for (WB_Circle c : circles) {
            centers.add(new WB_Point(c.getCenter()));
        }
        return centers;
    }

    public List<WB_Circle> getCircles(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId);
        List<WB_Circle> circles = new ArrayList<>();
        List list = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_CIRCLE);
        if (list != null) {
            for (Object o : list) {
                DXFCircle c = (DXFCircle) o;
                Point center = c.getCenterPoint();
                WB_Circle circle = new WB_Circle(new WB_Point(center.getX(), center.getY(), center.getZ()), c.getRadius());
                circles.add(circle);
            }
        }
        return circles;
    }

    public List<DXFText> getTexts(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId);
        List texts = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_TEXT);
        return texts == null ? new ArrayList<>() : texts;
    }

    public List<WB_Point> getWBPointFromGCD(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId);
        List<DXFText> texts = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_TEXT);
        List<WB_Point> points = new ArrayList<>();
        for (DXFText dxfPoint : texts)
            points.add(new WB_Point(dxfPoint.getInsertPoint().getX(), dxfPoint.getInsertPoint().getY(), Double.valueOf(dxfPoint.getText())));
        return texts == null ? new ArrayList<>() : points;
    }

    public List<WB_PolyLine> getLines(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId);
        List lines = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE);
        List<WB_PolyLine> polyLines = new ArrayList<>();
        if (lines != null) {
            for (Object o : lines) {
                DXFLine line = (DXFLine) o;
                polyLines.add(new WB_PolyLine(to_WB_Point(line.getStartPoint()), to_WB_Point(line.getEndPoint())));
            }
        }
        return polyLines;
    }

    private WB_Point to_WB_Point(Point p) {
        return new WB_Point(p.getX(), p.getY(), p.getZ());
    }

    public List<WB_Point> getPoints(String layerId) {
        DXFPoint_Reader reader = new DXFPoint_Reader(filePath);
        return reader.getPts(layerId);
    }

    public List<WB_Polygon> getPolygons(String layerId) {
        List<Z_PolyLine> lines = getDXFPolyLines(layerId);
        List<WB_Polygon> result = lines.stream().filter(e -> e.getPolygon() != null).map(Z_PolyLine::getPolygon).collect(Collectors.toList());
        result.addAll(get2DPolygon(layerId));
        return result.stream().filter(e -> e.getSignedArea() != 0).collect(Collectors.toList());
    }

//    public List<WB_Polygon> get3DPolygons(String layerId) {
//        List<Z_PolyLine> lines = getDXFPolyLines(layerId);
//        List<WB_Polygon> result = lines.stream().filter(e -> e.getPolygon() != null).map(Z_PolyLine::getPolygon).collect(Collectors.toList());
//        result.addAll(getDXFPolyLines(layerId));
//        return result.stream().filter(e -> e.getSignedArea() != 0).collect(Collectors.toList());
//    }


    public List<WB_PolyLine> getPolyLines(String layerId) {
        List<Z_PolyLine> lines = getDXFPolyLines(layerId);
        List<WB_PolyLine> result = lines.stream().filter(e -> e.getPolyLine() != null).map(Z_PolyLine::getPolyLine).collect(Collectors.toList());
        result.addAll(get2DPolyLine(layerId));
        return result;
    }


    static class Z_PolyLine {
        private final WB_PolyLine polyLine;
        private final WB_Polygon polygon;

        public Z_PolyLine(WB_PolyLine polyLine, WB_Polygon polygon) {
            this.polyLine = polyLine;
            this.polygon = polygon;
        }

        public WB_PolyLine getPolyLine() {
            return polyLine;
        }

        public WB_Polygon getPolygon() {
            return polygon;
        }
    }

    private List<Z_PolyLine> getDXFPolyLines(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId);
        List pLines = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LWPOLYLINE);
        List<Z_PolyLine> lines = new ArrayList<>();
        if (pLines != null) {
            for (Object o : pLines) {
                DXFPolyline pLine = (DXFPolyline) o;
                List<WB_Point> pts = new ArrayList<>();
                for (int i = 0; i < pLine.getVertexCount(); i++) {
                    DXFVertex vertex = pLine.getVertex(i);
                    pts.add(new WB_Point(vertex.getX(), vertex.getY(), vertex.getZ()));
                }
                if (pLine.isClosed()) {
                    lines.add(new Z_PolyLine(null, new WB_Polygon(pts)));
//                    System.out.println("Polygon [ Nodes = " + pts.size() + " , Closed = " + pLine.isClosed() + " ]");
                } else {
                    lines.add(new Z_PolyLine(new WB_PolyLine(pts), null));
//                    System.out.println("PolyLine [ Nodes = " + pts.size() + " , Closed = " + pLine.isClosed() + " ]");
                }
            }
        }
        return lines;
    }

    public List<Z_PolyLine> getDXF3DPolyLines(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId);
        List pLines = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE);
        List<Z_PolyLine> lines = new ArrayList<>();
        if (pLines != null) {
            for (Object o : pLines) {
                DXFPolyline pLine = (DXFPolyline) o;
                List<WB_Point> pts = new ArrayList<>();
                for (int i = 0; i < pLine.getVertexCount(); i++) {
                    DXFVertex vertex = pLine.getVertex(i);
                    pts.add(new WB_Point(vertex.getX(), vertex.getY(), vertex.getZ()));
                }
                if (pLine.isClosed()) {
                    lines.add(new Z_PolyLine(null, new WB_Polygon(pts)));
                } else {
                    lines.add(new Z_PolyLine(new WB_PolyLine(pts), null));
                }
            }
        }
        return lines;
    }

    public List<Z_PolyLine> getDXF2DPolyLines(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId);
        List pLines = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE);
        List<Z_PolyLine> lines = new ArrayList<>();
        if (pLines != null) {
            for (Object o : pLines) {
                DXFPolyline pLine = (DXFPolyline) o;
                List<WB_Point> pts = new ArrayList<>();
                for (int i = 0; i < pLine.getVertexCount(); i++) {
                    DXFVertex vertex = pLine.getVertex(i);
                    pts.add(new WB_Point(vertex.getX(), vertex.getY(), vertex.getZ()));
                }
                if (pLine.isClosed()) {
                    lines.add(new Z_PolyLine(null, new WB_Polygon(pts)));
//                    System.out.println("Polygon [ Nodes = " + pts.size() + " , Closed = " + pLine.isClosed() + " ]");
                } else {
                    lines.add(new Z_PolyLine(new WB_PolyLine(pts), null));
//                    System.out.println("PolyLine [ Nodes = " + pts.size() + " , Closed = " + pLine.isClosed() + " ]");
                }
            }
        }
        return lines;
    }
//
//    public List<WB_Polygon> get3DPolygon(String layerId) {
//
//    }

    public List<WB_Polygon> get2DPolygon(String layerId) {
        List<Z_PolyLine> lines = getDXF2DPolyLines(layerId);
        return lines.stream().filter(e -> e.getPolygon() != null).map(Z_PolyLine::getPolygon).collect(Collectors.toList());
    }

    public List<WB_PolyLine> get2DPolyLine(String layerId) {
        List<Z_PolyLine> lines = getDXF2DPolyLines(layerId);
        return lines.stream().filter(e -> e.getPolyLine() != null).map(Z_PolyLine::getPolyLine).collect(Collectors.toList());
    }


    /**
     * 将文件中的栅格图像参照的边界提取出来
     *
     * @param layerId
     * @return
     */
    public List<Bounds> getPicBounds(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId);
        List pics = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_IMAGE);
//        List pics = layer.getDXFEntities(DXFConstants.OBJECT_TYPE_IMAGEDEF);
        List<Bounds> boundsList = new ArrayList<>();

        if (pics != null) {
            for (Object o : pics) {
                DXFImage image = (DXFImage) o;
                boundsList.add(image.getBounds());
            }
        }

        return boundsList;
    }


    public List<Point> getPicPos(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId);
        List pics = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_IMAGE);
        List<Point> posList = new ArrayList<>();

        if (pics != null) {
            for (Object o : pics) {
                DXFImage image = (DXFImage) o;
                posList.add(image.getInsertPoint());
            }
        }

        return posList;
    }

    /**
     * 获取文件中的插入光栅参照的dxfImage
     *
     * @param layerId
     * @return
     */
    public List<DXFImage> getDxfImage(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId);
        List pics = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_IMAGE);
        List<DXFImage> imgList = new ArrayList<>();

        if (pics != null) {
            for (Object o : pics) {
                DXFImage image = (DXFImage) o;
                imgList.add(image);
            }
        }

        return imgList;
    }


    private void read() {
        InputStream in = getInputStream(filePath);
        Parser parser = ParserBuilder.createDefaultParser();
        if (!isUtf8(new File(filePath))) {
            this.encoding = GBK;
        } else {
            this.encoding = UTF_8;
        }
        try {
            parser.parse(in, encoding);
            doc = parser.getDocument();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private InputStream getInputStream(String filePath) {
        InputStream in = null;
        try {
            File file = new File(filePath);
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }

    public String getFilePath() {
        return filePath;
    }

    public static Boolean isUtf8(File file) {
        boolean isUtf8 = true;
        byte[] buffer = readByteArrayData(file);
        int end = buffer.length;
        for (int i = 0; i < end; i++) {
            byte temp = buffer[i];
            if ((temp & 0x80) == 0) {// 0xxxxxxx
                continue;
            } else if ((temp & 0xC0) == 0xC0 && (temp & 0x20) == 0) {// 110xxxxx 10xxxxxx
                if (i + 1 < end && (buffer[i + 1] & 0x80) == 0x80 && (buffer[i + 1] & 0x40) == 0) {
                    i = i + 1;
                    continue;
                }
            } else if ((temp & 0xE0) == 0xE0 && (temp & 0x10) == 0) {// 1110xxxx 10xxxxxx 10xxxxxx
                if (i + 2 < end && (buffer[i + 1] & 0x80) == 0x80 && (buffer[i + 1] & 0x40) == 0
                        && (buffer[i + 2] & 0x80) == 0x80 && (buffer[i + 2] & 0x40) == 0) {
                    i = i + 2;
                    continue;
                }
            } else if ((temp & 0xF0) == 0xF0 && (temp & 0x08) == 0) {// 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
                if (i + 3 < end && (buffer[i + 1] & 0x80) == 0x80 && (buffer[i + 1] & 0x40) == 0
                        && (buffer[i + 2] & 0x80) == 0x80 && (buffer[i + 2] & 0x40) == 0
                        && (buffer[i + 3] & 0x80) == 0x80 && (buffer[i + 3] & 0x40) == 0) {
                    i = i + 3;
                    continue;
                }
            }
            isUtf8 = false;
            break;
        }
        return isUtf8;
    }

    /**
     * 从文件中直接读取字节
     *
     * @param file
     * @return
     */
    public static byte[] readByteArrayData(File file) {
        byte[] rebyte = null;
        BufferedInputStream bis;
        ByteArrayOutputStream output;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            output = new ByteArrayOutputStream();
            byte[] byt = new byte[1024 * 4];
            int len;
            try {
                while ((len = bis.read(byt)) != -1) {
                    if (len < 1024 * 4) {
                        output.write(byt, 0, len);
                    } else
                        output.write(byt);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            rebyte = output.toByteArray();
            if (bis != null) {
                bis.close();
            }
            if (output != null) {
                output.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rebyte;
    }

}
