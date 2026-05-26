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
 * @copyright (c) 2026, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.entity.storage.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * JAXB adapter: {@code Date} ↔ ISO date string {@code "yyyy-MM-dd"}.
 */
public class DateStringAdapter extends XmlAdapter<String, Date> {

    private static final String FORMAT = "yyyy-MM-dd";

    @Override
    public Date unmarshal(String v) throws Exception {
        if (v == null || v.isEmpty()) return null;
        return new SimpleDateFormat(FORMAT).parse(v);
    }

    @Override
    public String marshal(Date v) {
        if (v == null) return null;
        return new SimpleDateFormat(FORMAT).format(v);
    }
}


