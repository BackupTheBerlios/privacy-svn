#!/bin/sh
#
#     This file is part of Diki.
#
#     Copyright (C) 2009 jtheuer
#     Please refer to the documentation for a complete list of contributors
#
#     Diki is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     (at your option) any later version.
#
#     Diki is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
#
#     You should have received a copy of the GNU General Public License
#     along with Diki.  If not, see <http://www.gnu.org/licenses/>.
#

# takes the first argument as URL and opens a remote connection to diki via http
PORTNUMBER=30013

wget -O /dev/null -o /dev/null http://localhost:$PORTNUMBER/api/newBookmark?url=$1
EV=$?

if [ $EV -ne "0" ] ; then
	echo "Could not connect to diki - is it running?"
fi

exit $EV