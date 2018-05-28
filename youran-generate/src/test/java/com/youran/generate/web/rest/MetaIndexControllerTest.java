package com.youran.generate.web.rest;

import com.youran.common.pojo.vo.ReplyVO;
import com.youran.common.util.JsonUtil;
import com.youran.generate.data.MetaIndexData;
import com.youran.generate.help.GenerateHelper;
import com.youran.generate.pojo.dto.MetaIndexAddDTO;
import com.youran.generate.pojo.dto.MetaIndexUpdateDTO;
import com.youran.generate.pojo.po.MetaEntityPO;
import com.youran.generate.pojo.po.MetaFieldPO;
import com.youran.generate.pojo.po.MetaIndexPO;
import com.youran.generate.pojo.po.MetaProjectPO;
import com.youran.generate.web.AbstractWebTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Title:
 * Description:
 * Author: cbb
 * Create Time:2017/5/12 14:35
 */
public class MetaIndexControllerTest extends AbstractWebTest {

    @Autowired
    private GenerateHelper generateHelper;

    private MetaProjectPO metaProject;
    private MetaEntityPO metaEntity;
    private MetaFieldPO metaField;

    @Before
    public void init(){
        this.metaProject = generateHelper.saveProjectExample();
        this.metaEntity = generateHelper.saveEntityExample(metaProject.getProjectId(),0);
        this.metaField = generateHelper.saveFieldExample(metaEntity.getEntityId());
    }

    @Test
    public void save() throws Exception {
        MetaFieldPO metaField2 = generateHelper.saveFieldExample(metaEntity.getEntityId());
        MetaIndexAddDTO addDTO = MetaIndexData.getAddDTO(metaField.getFieldId(),metaField2.getFieldId());
        restMockMvc.perform(post(getRootPath()+"/meta_index/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.toJSONString(addDTO)))
                .andExpect(jsonPath("$.code").value(is(ReplyVO.SUCCESS_CODE)));

    }

    @Test
    public void update() throws Exception {
        MetaFieldPO metaField2 = generateHelper.saveFieldExample(metaEntity.getEntityId());
        MetaIndexPO metaIndex = generateHelper.saveIndexExample(metaField.getFieldId(),metaField2.getFieldId());
        MetaIndexUpdateDTO updateDTO = MetaIndexData.getUpdateDTO(metaIndex,metaField.getFieldId());
        restMockMvc.perform(put(getRootPath()+"/meta_index/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.toJSONString(updateDTO)))
                .andExpect(jsonPath("$.code").value(is(ReplyVO.SUCCESS_CODE)));
    }


    @Test
    public void list() throws Exception {
        generateHelper.saveIndexExample(metaField.getFieldId());
        restMockMvc.perform(get(getRootPath()+"/meta_index/list")
                .param("entityId",metaEntity.getEntityId()+""))
                .andExpect(jsonPath("$.code").value(is(ReplyVO.SUCCESS_CODE)))
                .andExpect(jsonPath("$.data.length()").value(is(1)));
    }

    @Test
    public void show() throws Exception {
        MetaIndexPO metaIndex = generateHelper.saveIndexExample(metaField.getFieldId());

        restMockMvc.perform(get(getRootPath()+"/meta_index/{indexId}",metaIndex.getIndexId()))
                .andExpect(jsonPath("$.code").value(is(ReplyVO.SUCCESS_CODE)))
                .andExpect(jsonPath("$.data.indexId").value(is(metaIndex.getIndexId())));
    }

    @Test
    public void del() throws Exception {
        MetaIndexPO metaIndex = generateHelper.saveIndexExample(metaField.getFieldId());

        restMockMvc.perform(delete(getRootPath()+"/meta_index/{indexId}",metaIndex.getIndexId()))
                .andExpect(jsonPath("$.code").value(is(ReplyVO.SUCCESS_CODE)))
                .andExpect(jsonPath("$.data").value(is(1)));
    }



}
