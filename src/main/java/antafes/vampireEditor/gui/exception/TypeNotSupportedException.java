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

public class TypeNotSupportedException extends Exception
{
    private static final String message = "The given type is not supported.";
    private static final String messageWithType = "The type '%s' is not supported.";

    public TypeNotSupportedException()
    {
        super(message);
    }

    public TypeNotSupportedException(String type)
    {
        super(String.format(messageWithType, type));
    }

    public TypeNotSupportedException(String type, Throwable cause)
    {
        super(String.format(messageWithType, type), cause);
    }

    public TypeNotSupportedException(Throwable cause)
    {
        super(message, cause);
    }

    protected TypeNotSupportedException(Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected TypeNotSupportedException(
        String type, Throwable cause, boolean enableSuppression, boolean writableStackTrace
    ) {
        super(String.format(messageWithType, type), cause, enableSuppression, writableStackTrace);
    }
}
