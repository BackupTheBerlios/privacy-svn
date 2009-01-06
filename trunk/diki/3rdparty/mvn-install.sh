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

mvn install:install-file  -Dfile=ICOReader-1.04.jar \
                          -DgroupId=net.vburgh \
                          -DartifactId=icoreader \
                          -Dversion=1.04 \
                          -Dpackaging=jar \
                          -DgeneratePom=true


mvn install:install-file  -Dfile=prefuse.jar \
                          -DgroupId=org.prefuse \
                          -DartifactId=prefuse \
                          -Dversion=beta-20071021 \
                          -Dpackaging=jar \
                          -DgeneratePom=true

mvn install:install-file  -Dfile=prefuse-beta-source.jar \
                          -DgroupId=org.prefuse \
                          -DartifactId=prefuse \
                          -Dversion=beta-20071021 \
			-Dclassifier=sources \
                          -Dpackaging=jar \
                          -DgeneratePom=true
                          
                          
mvn install:install-file  -Dfile=bcpg-jdk16-141.jar \
                          -DgroupId=org.bouncycastle \
                          -DartifactId=pg \
                          -Dversion=1.4.1 \
                          -Dpackaging=jar \
                          -DgeneratePom=true
                          
                          
mvn install:install-file  -Dfile=bcprov-jdk16-141.jar \
                          -DgroupId=org.bouncycastle \
                          -DartifactId=provider \
                          -Dversion=1.4.1 \
                          -Dpackaging=jar \
                          -DgeneratePom=true

mvn install:install-file  -Dfile=bcpg-jdk16-141-source.jar                         \
                          -DgroupId=org.bouncycastle \
                          -DartifactId=pg \
                          -Dversion=1.4.1 \
                          -Dpackaging=jar \
                          -DgeneratePom=true \
			  -Dclassifier=sources
  