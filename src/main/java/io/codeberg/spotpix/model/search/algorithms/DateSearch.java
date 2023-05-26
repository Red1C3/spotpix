package io.codeberg.spotpix.model.search.algorithms;

import java.nio.file.attribute.FileTime;
import java.util.Date;

import io.codeberg.spotpix.model.images.Image;

public class DateSearch  implements SearchAlgorithm{
    private Date startDate,endDate;
    public DateSearch(Date startDate,Date endDate){
        this.startDate=startDate;
        this.endDate=endDate;
    }

    @Override
    public boolean match(Image img) {
        FileTime fileTime=img.getFileTime();
        Date fileDate=new Date(fileTime.toMillis());

        return fileDate.after(startDate) && fileDate.before(endDate);
    }
}
