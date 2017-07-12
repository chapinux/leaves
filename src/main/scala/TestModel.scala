package leaves
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

object TestModel extends App{

  override def main(args: Array[String]) = {
    Model(
      alphaShape=10.59941807525341,
      angle0=96.63897069447752,
      angle1=30.776886788545088,
      angle2=68.43080407457133,
      angle3=70.3884920358327,
      angle4=35.37364051941062,
      decreaseRate0=1.205849687380363,
      decreaseRate1=0.7492089717440723,
      decreaseRate2=0.7182279958399268,
      decreaseRate3=1.4946524181098886,
      decreaseRate4=0.6347907796042684,
      nbBifurcation0=2.9234608485133187.round.toInt,
      nbBifurcation1=5.475892663903268.round.toInt,
      nbBifurcation2=1.4869297901407048.round.toInt,
      nbBifurcation3=2.1435515566919214.round.toInt,
      nbBifurcation4=0.013732234709920233.round.toInt,
      sterilityRate0=0.6488179351577368,
      sterilityRate1=0.15669654775731498,
      sterilityRate2=0.6469189804756419,
      sterilityRate3=0.2982568350229477,
      sterilityRate4=0.6656592440555815,
      thickness0=1.1092094792698135,
      thickness1=5.568224191660408,
      thickness2=7.463144992375982,
      thickness3=9.775215134207098,
      thickness4=9.9435275933314,
      render = true
    )
  }
}
