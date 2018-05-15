/**
 * This file is part of Vampire Editor.
 *
 * Vampire Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vampire Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Vampire Editor. If not, see <http://www.gnu.org/licenses/>.
 *
 * @package Vampire Editor
 * @author Marian Pollzien <map@wafriv.de>
 * @copyright (c) 2018, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */
package vampireEditor.character;

/**
 * Generation object.
 *
 * @author Marian Pollzien
 */
public class Generation implements GenerationInterface {
    private int generation;
    private int maximumAttributes;
    private int maximumBloodStock;
    private int bloodPerRound;

    /**
     * Create a new generation object.
     *
     * @param generation
     * @param maximumAttributes
     * @param maximumBloodStock
     * @param bloodPerRound
     */
    public Generation(int generation, int maximumAttributes, int maximumBloodStock, int bloodPerRound) {
        this.generation = generation;
        this.maximumAttributes = maximumAttributes;
        this.maximumBloodStock = maximumBloodStock;
        this.bloodPerRound = bloodPerRound;
    }

    /**
     * Get the generation number.
     *
     * @return
     */
    @Override
    public int getGeneration() {
        return generation;
    }

    /**
     * Get the maximum for attributes.
     *
     * @return
     */
    @Override
    public int getMaximumAttributes() {
        return maximumAttributes;
    }

    /**
     * Get the maximum for the blood stock.
     *
     * @return
     */
    @Override
    public int getMaximumBloodStock() {
        return maximumBloodStock;
    }

    /**
     * Get the maximum allowed usage of blood per round.
     *
     * @return
     */
    @Override
    public int getBloodPerRound() {
        return bloodPerRound;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return
     */
    @Override
    public String toString() {
        return Integer.toString(generation);
    }
}
