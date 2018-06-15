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

  override def main(args: Array[String]) = {
    Model(
      alphaShape = 40.59941807525341,
      thickness = 50.5,
      decreaseRate = 0.705849687380363,
      angle = 1.52,
      nbBifurcation = 2,
      angleRate = 2.52 ,
      depth = 7,
      Some(File("/tmp/model.svg"))
    )
  }
}
