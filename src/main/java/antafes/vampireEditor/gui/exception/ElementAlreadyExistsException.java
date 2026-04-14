/*
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
 * @copyright (c) 2023, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.gui.exception;

import lombok.NonNull;

public class ElementAlreadyExistsException extends Exception
{
    private static final String message = "Element already exists.";
    private static final String messageWithElement = "Element '%s' already exists.";

    public ElementAlreadyExistsException()
    {
        super(message);
    }

    public ElementAlreadyExistsException(Throwable cause)
    {
        super(message, cause);
    }

    public ElementAlreadyExistsException(@NonNull String element)
    {
        super(String.format(messageWithElement, element));
    }

    public ElementAlreadyExistsException(@NonNull String element, Throwable cause)
    {
        super(String.format(messageWithElement, element), cause);
    }

    protected ElementAlreadyExistsException(
        String element,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace
    ) {
        super(String.format(messageWithElement, element), cause, enableSuppression, writableStackTrace);
    }
}
