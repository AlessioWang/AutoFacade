package Tools;

import wblut.geom.*;

import java.util.ArrayList;
import java.util.List;

public class Douglas {
    /**
     * �򻯼����Ķ��㼯��
     */
    private final List<WB_Coord> out;

    /**
     * �������λ����ԣ����м򻯼���
     *
     * @param line    ����λ�����
     * @param epsilon ���
     */
    public Douglas(WB_PolyLine line, double epsilon) {
        List<WB_Coord> pointList = line.getPoints().toList();
        if (line instanceof WB_Polygon || line.getPoint(0).equals(line.getPoint(line.getNumberOfPoints() - 1))) {
            sort(pointList);
            pointList.add(pointList.get(0));
        }
        out = new ArrayList<>();
        douglasCal(pointList, epsilon, out);
    }

    /**
     * �Զ���ζ������½������򣬱������ε�������߶���ĳ�������޷�����
     *
     * @param list �������εĶ���
     */
    private void sort(List<WB_Coord> list) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            WB_Point a = new WB_Point(list.get(i));
            WB_Point b = new WB_Point(list.get((i + 1) % list.size()));
            WB_Point c = new WB_Point(list.get((i + 2) % list.size()));
            WB_Vector x1 = b.subToVector3D(a);
            WB_Vector x2 = c.subToVector3D(b);
            if (x1.cross(x2).getLength() > 0.1)
                index = (i + 1) % list.size();
        }
        List<WB_Coord> coords = new ArrayList<>();
        if (index != -1) {
            for (int i = index; i < list.size(); i++)
                coords.add(list.get(i));
            for (int i = 0; i < index; i++)
                coords.add(list.get(i));
            list.clear();
            list.addAll(coords);
        }
    }

    /**
     * ��ȡ�����Ķ����
     *
     * @return ��������
     */
    public WB_PolyLine getAsPolyLine() {
        return new WB_PolyLine(out);
    }

    /**
     * ��ȡ�򻯼����Ķ����
     *
     * @return ��������
     */
    public WB_Polygon getAsPolygon() {
        out.remove(out.size() - 1);
        return new WB_Polygon(out);
    }

    /**
     * ���㴹ֱ����
     *
     * @param pt        �����
     * @param lineStart ��ʼ��
     * @param lineEnd   ������
     * @return ����
     */
    private double perpendicularDistance(WB_Coord pt, WB_Coord lineStart, WB_Coord lineEnd) {
        double dx = lineEnd.xd() - lineStart.xd();
        double dy = lineEnd.yd() - lineStart.yd();
        double dz = lineEnd.zd() - lineStart.zd();

        // Normalize
        double mag = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (mag > 0.0) {
            dx /= mag;
            dy /= mag;
            dz /= mag;
        }
        double pvx = pt.xd() - lineStart.xd();
        double pvy = pt.yd() - lineStart.yd();
        double pvz = pt.zd() - lineStart.zd();

        // Get dot product (project pv onto normalized direction)
        double pvdot = dx * pvx + dy * pvy + dz * pvz;

        // Scale line direction vector and subtract it from pv
        double ax = pvx - pvdot * dx;
        double ay = pvy - pvdot * dy;
        double az = pvz - pvdot * dz;

        return Math.sqrt(ax * ax + ay * ay + az * az);
    }

    /**
     * �������㣬���ж���λ����Եļ�
     *
     * @param pointList ����㼯
     * @param epsilon   �����
     * @param out       ������
     */
    private void douglasCal(List<WB_Coord> pointList, double epsilon, List<WB_Coord> out) {
        if (pointList.size() < 2)
            throw new IllegalArgumentException("Not enough points to simplify");

        // Find the point with the maximum distance from line between the start and end
        double dmax = 0.0;
        int index = 0;
        int end = pointList.size() - 1;
        for (int i = 1; i < end; ++i) {
            double d = perpendicularDistance(pointList.get(i), pointList.get(0), pointList.get(end));
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        // If max distance is greater than epsilon, recursively simplify
        if (dmax > epsilon) {
            List<WB_Coord> recResults1 = new ArrayList<>();
            List<WB_Coord> recResults2 = new ArrayList<>();
            List<WB_Coord> firstLine = pointList.subList(0, index + 1);
            List<WB_Coord> lastLine = pointList.subList(index, pointList.size());
            douglasCal(firstLine, epsilon, recResults1);
            douglasCal(lastLine, epsilon, recResults2);

            // build the result list
            out.addAll(recResults1.subList(0, recResults1.size() - 1));
            out.addAll(recResults2);
            if (out.size() < 2)
                throw new RuntimeException("Problem assembling output");
        } else {
            // Just return start and end points
            out.clear();
            out.add(pointList.get(0));
            out.add(pointList.get(pointList.size() - 1));
        }
    }
}