#!/bin/bash

mkdir res
cd res
wget --directory-prefix=./ --output-document=res.zip "https://drive.google.com/uc?export=download&id=1hr60Sq0t-JYoVmVxOsiHO-Pm658s4MAa"
unzip res.zip
rm res.zip
