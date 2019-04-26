package leaves

import better.files.File

/*
 * Copyright (C) 19/06/17 // mathieu.leclaire@openmole.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

object TestModel extends App {
//301: Exception in thread "main" org.locationtech.jts.geom.TopologyException: no outgoing dirEdge found [ (94.85901923076926, 289.1070000000004, NaN) ]
  //100,2.1273655180440656,0.619290143189155,8.96510645366539,4.774156656068007,0.013310358085883902,4.525613068272611,1190.393014145804,6146.8624605810255
  override def main(args: Array[String]) = {
    val (area, perimeter, length) = Model(
      thickness = 2.1273655180440656,
      decreaseRate = 0.619290143189155,
      angle = 8.96510645366539,
      nbBifurcation = 4.774156656068007.toInt,
      angleRate = 0.013310358085883902,
      depth = 4.525613068272611.toInt,
      Some(File("model.svg"))
    )
    println(s"area = $area, length = $length")
  }
}
