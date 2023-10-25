package com.rulin.media.service;

import com.rulin.base.model.PageParams;
import com.rulin.base.model.PageResult;
import com.rulin.base.model.RestResponse;
import com.rulin.media.model.dto.QueryMediaParamsDto;
import com.rulin.media.model.dto.UploadFileParamsDto;
import com.rulin.media.model.dto.UploadFileResultDto;
import com.rulin.media.model.po.MediaFiles;


/**
 * 媒资文件管理业务类
 *
 * @author 程序儒
 * @date 2023-08-25 18:28:40
 */
public interface MediaFileService {


    /**
     * 媒资文件查询方法
     *
     * @param companyId           公司标识
     * @param pageParams          页面参数
     * @param queryMediaParamsDto 查询媒体params dto
     * @return {@code PageResult<MediaFiles>}
     */
    public PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);


    /**
     * 上传文件
     *
     * @param companyId           机构id
     * @param uploadFileParamsDto 上传文件信息
     * @param localFilePath       文件磁盘路径
     * @return 文件信息
     */
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath);

    /**
     * @param companyId           机构id
     * @param fileMd5             文件md5值
     * @param uploadFileParamsDto 上传文件的信息
     * @param bucket              桶
     * @param objectName          对象名称
     * @return com.rulin.media.model.po.MediaFiles
     * @description 将文件信息添加到文件表
     */

    public MediaFiles addMediaFilesToDb(Long companyId, String fileMd5, UploadFileParamsDto uploadFileParamsDto, String bucket, String objectName);

    /**
     * @param fileMd5 文件的md5
     * @description 检查文件是否存在
     */
    public RestResponse<Boolean> checkFile(String fileMd5);

    /**
     * @param fileMd5    文件的md5
     * @param chunkIndex 分块序号
     * @description 检查分块是否存在
     */
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex);


    /**
     * @description 上传分块
     * @param fileMd5  文件md5
     * @param chunk  分块序号
     * @param localChunkFilePath  分块文件本地路径
     */
    public RestResponse uploadChunk(String fileMd5,int chunk,String localChunkFilePath);

    /**
     * @description 合并分块
     * @param companyId  机构id
     * @param fileMd5  文件md5
     * @param chunkTotal 分块总和
     * @param uploadFileParamsDto 文件信息
     */
    public RestResponse mergechunks(Long companyId,String fileMd5,int chunkTotal,UploadFileParamsDto uploadFileParamsDto);

    MediaFiles getFileById(String mediaId);
}
