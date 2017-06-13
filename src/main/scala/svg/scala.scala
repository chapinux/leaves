
/*
 * Copyright (C) 13/06/17 // mathieu.leclaire@openmole.org
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

package object svg {

  object path {

    def start(x: Int, y: Int): Path = apply("<svg width=\"800\" height=\"800\" xmlns=\"http://www.w3.org/2000/svg\">\n<path d=\"").m(x, y)

    def apply(st: String = "") = new Path {
      def svgString = st
    }

    type PathOperator = String
    val M: PathOperator = "M"
    val L: PathOperator = "L"
    val H: PathOperator = "H"
    val V: PathOperator = "V"
    val C: PathOperator = "C"
    val Q: PathOperator = "Q"
    val S: PathOperator = "S"
    val T: PathOperator = "T"
    val A: PathOperator = "A"
    val Z: PathOperator = "Z"

    trait Path {
      def svgString: String

      private def append(s: String): Path = path(svgString + s" $s")

      def end: Path = path(svgString + "\" stroke=\"black\"/>\n</svg>")

      def m(x: Int, y: Int): Path = append(s"$M $x $y")

      def l(x: Int, y: Int): Path = append(s"$L $x $y")

      def ml(mx: Int, my: Int, lx: Int, ly: Int) = m(mx,my).l(lx,ly)

      def h(y: Int): Path = append(s"$H $y")

      def v(x: Int): Path = append(s"$V $x")

      def c(x1: Int, y1: Int, x2: Int, y2: Int, x: Int, y: Int): Path = append(s"$C $x1 $y1 $x2 $y2 $x $y")

      def q(x1: Int, y1: Int, x: Int, y: Int): Path = append(s"$Q $x1 $y1 $x $y")

      def s(x2: Int, y2: Int, x: Int, y: Int): Path = append(s"$S $x2 $y2 $x $y")

      def t(x: Int, y: Int): Path = append(s"$T $x $y")

      def a(rx: Int, ry: Int, xAxisRotation: Int, largeArcFlag: Int, sweepFlag: Int, x: Double, y: Double) = append(s"$A $rx $ry $xAxisRotation $largeArcFlag $sweepFlag $x $y")

      def z = append("Z")
    }

  }

}